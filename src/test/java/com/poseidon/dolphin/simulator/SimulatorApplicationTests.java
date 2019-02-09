package com.poseidon.dolphin.simulator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.Contract;
import com.poseidon.dolphin.simulator.account.PaymentFrequency;
import com.poseidon.dolphin.simulator.account.service.AccountService;
import com.poseidon.dolphin.simulator.member.Member;
import com.poseidon.dolphin.simulator.product.Interest;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductType;
import com.poseidon.dolphin.simulator.timeline.Activity;
import com.poseidon.dolphin.simulator.timeline.Timeline;
import com.poseidon.dolphin.simulator.timeline.service.TimelineService;
import com.poseidon.dolphin.simulator.wallet.Wallet;
import com.poseidon.dolphin.simulator.wallet.WalletLog;
import com.poseidon.dolphin.simulator.wallet.service.WalletService;

@RunWith(SpringRunner.class)
public class SimulatorApplicationTests {
	private SimulatorApplication application;
	
	@MockBean
	private TimelineService timelineService;
	
	@MockBean
	private AccountService accountService;
	
	@MockBean
	private WalletService walletService;
	
	private Member member;
	
	@Before
	public void setUp() {
		application = new SimulatorApplication(timelineService, accountService, walletService);
		
		member = new Member();
		member.setUsername("alice");
	}
	
	@Test
	public void givenMemberThenCloseAccount() {
		given(timelineService.next(any(Member.class))).willReturn(null);
		
		assertThat(application.closeAccount(member)).isFalse();
		
		Timeline timeline = new Timeline();
		timeline.setActivity(Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT);
		given(timelineService.next(any(Member.class))).willReturn(timeline);
		
		assertThat(application.closeAccount(member)).isFalse();
		
		timeline = new Timeline();
		timeline.setActivity(Activity.INSTALLMENT_SAVING_ACCOUNT_CLOSE);
		timeline.setReferenceId(10l);
		given(timelineService.next(any(Member.class))).willReturn(timeline);
		given(timelineService.done(any(Member.class))).willReturn(timeline);
		
		Account account = new Account();
		account.setId(1l);
		Contract contract = new Contract();
		contract.setProductType(ProductType.INSTALLMENT_SAVING);
		contract.setInterest(Interest.DAY_SIMPLE);
		contract.setContractDate(LocalDate.now());
		contract.setExpiryDate(contract.getContractDate().plusMonths(12));
		contract.setPaymentFrequency(PaymentFrequency.MONTH);
		account.setContract(contract);
		account.setLastModifiedDate(LocalDateTime.now());
		given(accountService.closeAccount(anyLong())).willReturn(account);
		
		Wallet wallet = new Wallet();
		given(walletService.save(any(Member.class), anyLong(), any(WalletLog.class))).willReturn(wallet);
		
		assertThat(application.closeAccount(member)).isTrue();
		
		verify(timelineService, times(3)).next(any(Member.class));
		verify(timelineService, times(1)).done(any(Member.class));
	}
	
	@Test
	public void givenAccountThenOpenAccount() {
		Account account = new Account();
		account.setId(10l);
		account.setMember(member);
		
		Product product = new Product();
		account.setProduct(product);
		
		Contract contract = new Contract();
		contract.setProductType(ProductType.FIXED_DEPOSIT);
		contract.setContractDate(LocalDate.now());
		contract.setExpiryDate(contract.getContractDate().plusMonths(12));
		account.setContract(contract);
		given(accountService.openAccount(any(Member.class), any(Product.class), any(Contract.class))).willReturn(account);
		
		List<Timeline> timelines = new ArrayList<>();
		given(timelineService.saveAll(any())).willReturn(timelines);
		
		Wallet wallet = new Wallet();
		given(walletService.save(any(Member.class), anyLong(), any(WalletLog.class))).willReturn(wallet);
		
		application.openAccount(account);
		
		verify(accountService, times(1)).openAccount(any(Member.class), any(Product.class), any(Contract.class));
		verify(timelineService, times(1)).saveAll(any());
		verify(walletService, times(1)).save(any(Member.class), anyLong(), any(WalletLog.class));
	}
	
	@Test
	public void givenMemberThenProceedRegularPayment() {
		Member member = new Member();
		given(accountService.findInstallmentSavingAccountByMember(any(Member.class))).willReturn(Optional.empty());
		try {
			application.proceedRegularPayAndTimelineDone(member);
			fail();
		} catch(IllegalArgumentException e) {}
		
		Account account = new Account();
		account.setId(1l);
		given(accountService.findInstallmentSavingAccountByMember(any(Member.class))).willReturn(Optional.of(account));
		given(accountService.regularyPay(anyLong(), any(Member.class))).willReturn(false);
		
		assertThat(application.proceedRegularPayAndTimelineDone(member)).isFalse();
		
		given(accountService.regularyPay(anyLong(), any(Member.class))).willReturn(true);
		
		assertThat(application.proceedRegularPayAndTimelineDone(member)).isTrue();
		
		verify(accountService, times(3)).findInstallmentSavingAccountByMember(any(Member.class));
		verify(accountService, times(2)).regularyPay(anyLong(), any(Member.class));
	}
	
	@Test
	public void givenMemberThenDepositProtectionCondition() {
		List<Account> accounts = new ArrayList<>();
		Account account = new Account();
		Product product = new Product();
		product.setCompanyNumber("KB국민은행금융회사번호");
		account.setProduct(product);
		
		Contract contract = new Contract();
		contract.setBalance(500000);
		contract.setProductType(ProductType.INSTALLMENT_SAVING);
		contract.setContractDate(LocalDate.of(2019, 1, 1));
		contract.setExpiryDate(contract.getContractDate().plusMonths(12));
		contract.setPaymentFrequency(PaymentFrequency.MONTH);
		account.setContract(contract);
		accounts.add(account);
		
		account = new Account();
		product = new Product();
		product.setCompanyNumber("KB국민은행금융회사번호");
		account.setProduct(product);
		
		contract = new Contract();
		contract.setBalance(40000000);
		contract.setProductType(ProductType.FIXED_DEPOSIT);
		contract.setContractDate(LocalDate.of(2019, 11, 1));
		contract.setExpiryDate(contract.getContractDate().plusMonths(12));
		contract.setPaymentFrequency(PaymentFrequency.MONTH);
		account.setContract(contract);
		accounts.add(account);
		given(accountService.findAllByMember(any(Member.class))).willReturn(accounts);
		
		long balance = 1800000;
		Set<String> companyNumbers = application.excludeCompaniesToSearchProducts(member, ProductType.INSTALLMENT_SAVING, balance);
		assertThat(companyNumbers).containsOnly(product.getCompanyNumber());
		
		companyNumbers = application.excludeCompaniesToSearchProducts(member, ProductType.FIXED_DEPOSIT, balance);
		assertThat(companyNumbers).isEmpty();
	}
	
}
