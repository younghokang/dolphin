package com.poseidon.dolphin.simulator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.service.AccountService;
import com.poseidon.dolphin.simulator.member.Member;
import com.poseidon.dolphin.simulator.member.SocialType;
import com.poseidon.dolphin.simulator.member.service.MemberService;
import com.poseidon.dolphin.simulator.product.InterestRate;
import com.poseidon.dolphin.simulator.product.InterestRateType;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductOption;
import com.poseidon.dolphin.simulator.product.ProductType;
import com.poseidon.dolphin.simulator.product.ReserveType;
import com.poseidon.dolphin.simulator.product.service.ProductService;
import com.poseidon.dolphin.simulator.timeline.Timeline;
import com.poseidon.dolphin.simulator.timeline.service.TimelineService;
import com.poseidon.dolphin.simulator.wallet.Wallet;
import com.poseidon.dolphin.simulator.wallet.service.WalletService;

@RunWith(SpringRunner.class)
public class SimulatorControllerTests {
	
	private MockMvc mvc;
	
	@MockBean
	private SimulatorApplication application;
	
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
		
		mvc = MockMvcBuilders.standaloneSetup(new SimulatorController(application, productService, accountService, timelineService, memberService, walletService))
				.setCustomArgumentResolvers(memberArgumentResolver)
				.build();
		
		TestingAuthenticationToken token = new TestingAuthenticationToken(member, null);
        SecurityContextHolder.getContext().setAuthentication(token);
	}
	
	@Test
	public void whenLoadSimulatorPageThenResponseSuccess() throws Exception {
		given(accountService.findInstallmentSavingAccountByMember(any(Member.class))).willReturn(Optional.of(new Account()));
		given(timelineService.availableRegulaPayment(any(Member.class))).willReturn(true);
		
		mvc.perform(get("/simulator"))
			.andExpect(status().isOk())
			.andExpect(view().name("simulator/index"))
			.andExpect(model().attributeExists("accountCommand"))
			.andExpect(model().attributeExists("member"))
			.andDo(print());
		
		given(accountService.findInstallmentSavingAccountByMember(any(Member.class))).willReturn(Optional.empty());
		given(accountService.findAllByMember(any(Member.class))).willReturn(null);
		
		mvc.perform(get("/simulator"))
			.andExpect(status().isOk())
			.andExpect(view().name("simulator/searchSavingProductsForm"))
			.andExpect(model().attributeExists("accountCommand"))
			.andExpect(model().attribute("savingAccounts", nullValue()))
			.andExpect(model().attribute("depositAccounts", nullValue()))
			.andDo(print());
		
		verify(accountService, times(2)).findInstallmentSavingAccountByMember(any(Member.class));
		verify(accountService, times(1)).findAllByMember(any(Member.class));
		verify(timelineService, times(1)).availableRegulaPayment(any(Member.class));
		
	}
	
	@Test
	public void whenSubmitParametersThenRedirectSimulatorIndexPage() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("balance", Long.toString(Product.DEFAULT_MIN_BALANCE));
        
        List<Product> products = new ArrayList<>();
        given(productService.getFilteredProductList(any(ProductType.class), anyLong(), anySet())).willReturn(products);
        
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
        
        verify(productService, times(1)).getFilteredProductList(any(ProductType.class), anyLong(), anySet());
	}
	
	@Test
	public void whenFormSubmitWithBalanceAndProductIdThenOpenAccount() throws Exception {
		long balance = Product.DEFAULT_MIN_BALANCE;
		long productId = 450l;
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("balance", Long.toString(balance));
        params.add("productId", Long.toString(productId));
        
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
        given(productService.getFilteredProductById(anyLong(), anyLong())).willReturn(product);
        
        Account account = new Account();
        given(application.openAccount(any(Account.class))).willReturn(account);
        
		mvc.perform(post("/simulator/openAccount")
				.params(params))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/simulator"))
			.andExpect(flash().attribute("message", equalTo("simulator.openAccountSuccess")))
			.andDo(print());
		
		verify(productService, times(1)).getFilteredProductById(anyLong(), anyLong());
		verify(application, times(1)).openAccount(any(Account.class));
	}
	
	@Test
	public void whenRequestNextTurnThenProcessTimeline() throws Exception {
		Timeline timeline = new Timeline();
		timeline.setActiveDate(LocalDate.now());
		given(timelineService.next(any(Member.class))).willReturn(timeline);
		
		given(memberService.changeCurrent(any(Member.class), any(LocalDate.class))).willReturn(member);
		
		mvc.perform(get("/simulator/nextTurn"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/simulator"))
			.andDo(print());
		
		verify(timelineService, times(1)).next(any(Member.class));
		verify(memberService, times(1)).changeCurrent(any(Member.class), any(LocalDate.class));
	}
	
	@Test
	public void whenRequestCloseAccountThenClosed() throws Exception {
		given(application.closeAccount(any(Member.class))).willReturn(true);
		
		mvc.perform(get("/simulator/closeAccount"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/simulator"))
			.andExpect(flash().attribute("message", equalTo("simulator.closeAccountSuccess")))
			.andDo(print());
		
		verify(application, times(1)).closeAccount(any(Member.class));
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
