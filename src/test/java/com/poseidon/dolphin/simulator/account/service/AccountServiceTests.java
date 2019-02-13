package com.poseidon.dolphin.simulator.account.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.dolphin.member.Member;
import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.AccountDetail;
import com.poseidon.dolphin.simulator.account.Contract;
import com.poseidon.dolphin.simulator.account.NotFoundAccountException;
import com.poseidon.dolphin.simulator.account.PaymentFrequency;
import com.poseidon.dolphin.simulator.account.State;
import com.poseidon.dolphin.simulator.account.repository.AccountRepository;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductType;

@RunWith(SpringRunner.class)
public class AccountServiceTests {
	private AccountService accountService;
	
	@MockBean
	private AccountRepository accountRepository;
	
	@Before
	public void setUp() {
		accountService = new AccountServiceImpl(accountRepository);
	}
	
	@Test
	public void whenCallOpenAccountThenReturnNewAccount() {
		Account account = new Account();
		account.setState(State.ACTIVE);
		given(accountRepository.save(any(Account.class))).willReturn(account);
		
		Member member = new Member();
		Product product = new Product();
		Contract contract = new Contract();
		Account savedAccount = accountService.openAccount(member, product, contract);
		assertThat(savedAccount).isNotNull();
		assertThat(savedAccount.getState()).isEqualTo(State.ACTIVE);
		
		verify(accountRepository, times(1)).save(any(Account.class));
	}
	
	@Test
	public void givenMemberThenReturnAccounts() {
		Member member = new Member();
		member.setUsername("afraid86");
		
		List<Account> accounts = new ArrayList<>();
		Account account = new Account();
		account.setMember(member);
		account.setState(State.ACTIVE);
		accounts.add(account);
		
		given(accountRepository.findAllByMemberAndState(any(Member.class), any(State.class))).willReturn(accounts);
		
		List<Account> savedAccounts = accountService.findAllByMember(member);
		assertThat(savedAccounts.size()).isEqualTo(1);
		
		verify(accountRepository, atLeastOnce()).findAllByMemberAndState(member, State.ACTIVE);
	}
	
	@Test
	public void givenRegularyPayThenReturnAccountDetails() {
		long id = 1l;
		long balance = 100000;
		String username = "afraid86";
		LocalDate contractDate = LocalDate.of(2019, 1, 1);
		
		Member member = new Member();
		member.setUsername(username);
		
		Account account = new Account();
		account.setMember(member);
		account.setState(State.ACTIVE);
		
		Contract contract = new Contract();
		contract.setBalance(balance);
		contract.setContractDate(contractDate);
		contract.setExpiryDate(contractDate.plusMonths(12));
		contract.setDateOfPayment(30);
		contract.setPaymentFrequency(PaymentFrequency.MONTH);
		account.setContract(contract);
		
		account.getAccountDetails().add(new AccountDetail(12, balance, contractDate));
		
		Account inActiveAccount = new Account();
		inActiveAccount.setMember(member);
		inActiveAccount.setState(State.INACTIVE);
		
		given(accountRepository.findById(2l)).willReturn(Optional.empty());
		assertThat(accountService.regularyPay(2l, member)).isFalse();
		
		given(accountRepository.findById(3l)).willReturn(Optional.of(inActiveAccount));
		assertThat(accountService.regularyPay(3l, member)).isFalse();
		
		given(accountRepository.findById(id)).willReturn(Optional.of(account));
		assertThat(accountService.regularyPay(id, new Member())).isFalse();
		
		assertThat(accountService.regularyPay(id, member)).isFalse();
		
		verify(accountRepository, times(4)).findById(anyLong());
	}
	
	@Test
	public void whenCallCloseAccountThenChangeState() {
		long id = 10l;
		Account account = new Account();
		account.setState(State.CLOSE);
		
		given(accountRepository.findById(anyLong())).willReturn(Optional.empty());
		
		try {
			accountService.closeAccount(id);
			fail();
		} catch(NotFoundAccountException e) {}
		
		given(accountRepository.findById(anyLong())).willReturn(Optional.of(account));
		given(accountRepository.save(any(Account.class))).willReturn(account);
		
		Account closedAccount = accountService.closeAccount(id);
		assertThat(closedAccount).isNotNull();
		assertThat(closedAccount.getState()).isEqualTo(State.CLOSE);
		
		verify(accountRepository, times(2)).findById(anyLong());
		verify(accountRepository, times(1)).save(any(Account.class));
	}
	
	@Test
	public void givenMemberThenFindInstallmentSavingAccount() {
		List<Account> accounts = new ArrayList<>();
		Account account = new Account();
		Contract contract = new Contract();
		contract.setProductType(ProductType.FIXED_DEPOSIT);
		account.setContract(contract);
		accounts.add(account);
		given(accountRepository.findAllByMemberAndState(any(Member.class), any(State.class))).willReturn(accounts);
		
		Member member = new Member();
		assertThat(accountService.findInstallmentSavingAccountByMember(member)).isEmpty();
		
		account = new Account();
		contract = new Contract();
		contract.setProductType(ProductType.INSTALLMENT_SAVING);
		account.setContract(contract);
		accounts.add(account);
		given(accountRepository.findAllByMemberAndState(any(Member.class), any(State.class))).willReturn(accounts);
		
		assertThat(accountService.findInstallmentSavingAccountByMember(member)).isNotEmpty();
		
		verify(accountRepository, times(2)).findAllByMemberAndState(any(Member.class), any(State.class));
	}
	
}
