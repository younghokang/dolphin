package com.poseidon.dolphin.simulator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.poseidon.dolphin.config.MemberArgumentResolver;
import com.poseidon.dolphin.member.Member;
import com.poseidon.dolphin.member.SocialType;
import com.poseidon.dolphin.member.service.MemberService;
import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.Contract;
import com.poseidon.dolphin.simulator.account.PaymentFrequency;
import com.poseidon.dolphin.simulator.account.service.AccountService;
import com.poseidon.dolphin.simulator.product.Interest;
import com.poseidon.dolphin.simulator.product.InterestRate;
import com.poseidon.dolphin.simulator.product.InterestRateType;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductOption;
import com.poseidon.dolphin.simulator.product.ProductType;
import com.poseidon.dolphin.simulator.product.ReserveType;
import com.poseidon.dolphin.simulator.product.service.ProductService;
import com.poseidon.dolphin.simulator.timeline.Activity;
import com.poseidon.dolphin.simulator.timeline.Timeline;
import com.poseidon.dolphin.simulator.timeline.service.TimelineService;
import com.poseidon.dolphin.simulator.wallet.Wallet;
import com.poseidon.dolphin.simulator.wallet.WalletLog;
import com.poseidon.dolphin.simulator.wallet.service.WalletService;

@RunWith(SpringRunner.class)
public class SimulatorControllerTests {
	
	private MockMvc mvc;
	
	@MockBean
	private ProductService productService;
	
	@MockBean
	private AccountService accountService;
	
	@MockBean
	private TimelineService timelineService;
	
	@MockBean
	private MemberService memberService;
	
	@MockBean
	private WalletService walletService;
	
	private Member member;
	
	private MemberArgumentResolver memberArgumentResolver;
	
	@Before
	public void setUp() {
		member = new Member();
		member.setUsername("1234567890");
		member.setEmail("afraid86");
		member.setCurrent(LocalDate.of(2019, 1, 1));
		member.setSocialType(SocialType.NAVER);
		
		given(memberService.loadByUsername(anyString())).willReturn(member);
		memberArgumentResolver = new MemberArgumentResolver(memberService);
		
		mvc = MockMvcBuilders.standaloneSetup(new SimulatorController(productService, accountService, timelineService, memberService, walletService))
				.setCustomArgumentResolvers(memberArgumentResolver)
				.build();
		
		TestingAuthenticationToken token = new TestingAuthenticationToken(member, null);
        SecurityContextHolder.getContext().setAuthentication(token);
	}
	
	@Test
	public void whenLoadSimulatorPageThenResponseSuccess() throws Exception {
		given(accountService.findInstallmentSavingAccountByMember(any(Member.class))).willReturn(Optional.empty());
		
		mvc.perform(get("/simulator"))
			.andExpect(status().isOk())
			.andExpect(view().name("simulator/searchSavingProductsForm"))
			.andExpect(model().attributeExists("accountCommand"))
			.andDo(print());
		
		Account account = new Account();
		account.setId(10l);
		given(accountService.findInstallmentSavingAccountByMember(any(Member.class))).willReturn(Optional.of(account));
		given(timelineService.availableRegulaPayment(any(Member.class))).willReturn(false);
		given(accountService.findAllByMember(any(Member.class))).willReturn(Collections.emptyList());
		
		mvc.perform(get("/simulator"))
			.andExpect(status().isOk())
			.andExpect(view().name("simulator/index"))
			.andExpect(model().attributeExists("savingAccounts", "depositAccounts", "accountCommand", "member"))
			.andDo(print());
		
		given(timelineService.availableRegulaPayment(any(Member.class))).willReturn(true);
		given(accountService.regularyPay(anyLong(), any(Member.class))).willReturn(false);
		
		mvc.perform(get("/simulator"))
			.andExpect(status().isOk())
			.andExpect(view().name("simulator/index"))
			.andExpect(model().attributeExists("savingAccounts", "depositAccounts", "accountCommand", "member"))
			.andDo(print());
		
		given(accountService.regularyPay(anyLong(), any(Member.class))).willReturn(true);
		given(timelineService.done(any(Member.class))).willReturn(null);
		
		mvc.perform(get("/simulator"))
			.andExpect(status().isOk())
			.andExpect(view().name("simulator/index"))
			.andExpect(model().attributeExists("savingAccounts", "depositAccounts", "accountCommand", "member"))
			.andDo(print());
		
		verify(accountService, times(4)).findInstallmentSavingAccountByMember(any(Member.class));
		verify(timelineService, times(3)).availableRegulaPayment(any(Member.class));
		verify(accountService, times(6)).findAllByMember(any(Member.class));
		verify(accountService, times(2)).regularyPay(anyLong(), any(Member.class));
		verify(timelineService, times(1)).done(any(Member.class));
	}
	
	@Test
	public void whenSubmitParametersThenRedirectSimulatorIndexPage() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("balance", Long.toString(Product.DEFAULT_MIN_BALANCE));
        
        List<Product> products = new ArrayList<>();
        given(productService.findAllByProductTypeAndTestState(any(ProductType.class), any(TestState.class))).willReturn(products);
        
        mvc.perform(post("/simulator/searchSavingProductsForm")
                .params(params))
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name("simulator/searchSavingProductsForm"))
            .andExpect(model().attributeExists("products"))
            .andDo(print());
        
        params.set("balance", Long.toString(1000l));
        
        mvc.perform(post("/simulator/searchSavingProductsForm")
                .params(params))
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name("simulator/searchSavingProductsForm"))
            .andExpect(model().hasErrors())
            .andExpect(model().attributeDoesNotExist("products"))
            .andDo(print());
        
        verify(productService, times(1)).findAllByProductTypeAndTestState(any(ProductType.class), any(TestState.class));
	}
	
	@Test
	public void whenFormSubmitWithBalanceAndProductIdThenOpenAccount() throws Exception {
		long balance = 100;
		long productId = 450l;
		ProductType productType = ProductType.INSTALLMENT_SAVING;
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("balance", Long.toString(balance));
        params.add("productId", Long.toString(productId));
        params.add("productType", productType.name());
        
		mvc.perform(post("/simulator/openAccount")
				.params(params))
			.andExpect(status().is2xxSuccessful())
			.andExpect(forwardedUrl("simulator/searchSavingProductsForm"))
			.andExpect(model().hasErrors())
			.andExpect(model().attributeHasFieldErrors("accountCommand", "balance"))
			.andDo(print());
		
		balance = Product.DEFAULT_MIN_BALANCE;
		productId = 0l;
		
		params = new LinkedMultiValueMap<>();
        params.add("balance", Long.toString(balance));
        params.add("productId", Long.toString(productId));
        params.add("productType", productType.name());
		
		mvc.perform(post("/simulator/openAccount")
				.params(params))
			.andExpect(status().is2xxSuccessful())
			.andExpect(forwardedUrl("simulator/searchSavingProductsForm"))
			.andExpect(model().hasErrors())
			.andExpect(model().attributeHasFieldErrors("accountCommand", "productId"))
			.andDo(print());
		
		productId = 450l;
		params = new LinkedMultiValueMap<>();
        params.add("balance", Long.toString(balance));
        params.add("productId", Long.toString(productId));
        params.add("productType", productType.name());
        
        Product product = new Product();
        product.setProductType(ProductType.INSTALLMENT_SAVING);
        product.setName("product");
        
        List<ProductOption> productOptions = new ArrayList<>();
        ProductOption productOption = new ProductOption();
        productOption.setId(1l);
        
        List<InterestRate> interestRates = Arrays.asList(new InterestRate(Product.DEFAULT_PERIOD, 0.025, 0.027));
        productOption.setInterestRates(interestRates);
        productOption.setInterestRateType(InterestRateType.SIMPLE);
        productOption.setReserveType(ReserveType.FIXED);
        productOptions.add(productOption);
        product.setProductOptions(productOptions);
        product.setFilteredOptionId(1l);
        given(productService.findById(anyLong())).willReturn(product);
        
        Account account = new Account();
        account.setId(10l);
        
        Contract contract = new Contract();
        contract.setProductType(productType);
        contract.setContractDate(LocalDate.now());
        contract.setExpiryDate(contract.getContractDate().plusMonths(12));
        contract.setPaymentFrequency(PaymentFrequency.MONTH);
        contract.setDateOfPayment(10);
        account.setContract(contract);
        
        Member member = new Member();
        account.setMember(member);
        given(accountService.openAccount(any(Member.class), any(Product.class), any(Contract.class))).willReturn(account);
        
        List<Timeline> timelines = new ArrayList<>();
		given(timelineService.saveAll(any())).willReturn(timelines);
		
		mvc.perform(post("/simulator/openAccount")
				.params(params))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/simulator"))
			.andExpect(flash().attribute("message", "simulator.openAccountSuccess"))
			.andDo(print());
		
		verify(productService, times(1)).findById(anyLong());
		verify(accountService, times(1)).openAccount(any(Member.class), any(Product.class), any(Contract.class));
		verify(timelineService, times(1)).saveAll(any());
	}
	
	@Test
	public void whenRequestNextTurnThenProcessTimeline() throws Exception {
		Timeline timeline = new Timeline();
		timeline.setActiveDate(LocalDate.now());
		timeline.setActivity(Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT);
		given(timelineService.next(any(Member.class))).willReturn(timeline);
		
		given(memberService.saveChanges(any(Member.class))).willReturn(member);
		
		mvc.perform(get("/simulator/nextTurn"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/simulator"))
			.andDo(print());
		
		verify(timelineService, times(1)).next(any(Member.class));
		verify(memberService, times(1)).saveChanges(any(Member.class));
	}
	
	@Test
	public void whenRequestCloseAccountThenClosed() throws Exception {
		given(timelineService.next(any(Member.class))).willReturn(null);
		
		mvc.perform(get("/simulator/closeAccount"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/simulator"))
			.andExpect(flash().attribute("message", equalTo("simulator.closeAccountFail")))
			.andDo(print());
		
		Timeline timeline = new Timeline();
		timeline.setActivity(Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT);
		given(timelineService.next(any(Member.class))).willReturn(timeline);
		
		mvc.perform(get("/simulator/closeAccount"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/simulator"))
			.andExpect(flash().attribute("message", equalTo("simulator.closeAccountFail")))
			.andDo(print());
		
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
		
		mvc.perform(get("/simulator/closeAccount"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/simulator"))
			.andExpect(flash().attribute("message", equalTo("simulator.closeAccountSuccess")))
			.andDo(print());
		
		verify(timelineService, times(3)).next(any(Member.class));
		verify(timelineService, times(3)).next(any(Member.class));
		verify(timelineService, times(1)).done(any(Member.class));
		
	}
	
	@Test
	public void whenMoveSearchDepositProductsFormThenLoadProductsByWalletBalance() throws Exception {
		Wallet wallet = new Wallet();
		wallet.setBalance(5000000l);
		given(walletService.getMyWallet(any(Member.class))).willReturn(wallet);
		
		mvc.perform(get("/simulator/searchDepositProductsForm"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(model().attributeExists("accountCommand", "products"))
			.andDo(print());
		
		verify(walletService, times(2)).getMyWallet(any(Member.class));
	}
	
}
