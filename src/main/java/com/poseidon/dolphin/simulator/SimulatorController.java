package com.poseidon.dolphin.simulator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.Contract;
import com.poseidon.dolphin.simulator.account.service.AccountService;
import com.poseidon.dolphin.simulator.calculator.InterestCalculator;
import com.poseidon.dolphin.simulator.command.AccountCommand;
import com.poseidon.dolphin.simulator.member.Member;
import com.poseidon.dolphin.simulator.member.service.MemberService;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductType;
import com.poseidon.dolphin.simulator.product.service.ProductService;
import com.poseidon.dolphin.simulator.timeline.Timeline;
import com.poseidon.dolphin.simulator.timeline.service.TimelineService;
import com.poseidon.dolphin.simulator.wallet.Wallet;
import com.poseidon.dolphin.simulator.wallet.service.WalletService;

@Controller
@RequestMapping("/simulator")
public class SimulatorController {
	private final SimulatorApplication application;
	private final ProductService productService;
	private final AccountService accountService;
	private final TimelineService timelineService;
	private final MemberService memberService;
	private final WalletService walletService;
	
	@Autowired
	public SimulatorController(SimulatorApplication application, ProductService productService, 
			AccountService accountService, TimelineService timelineService,
			MemberService memberService, WalletService walletService) {
		this.application = application;
		this.productService = productService;
		this.accountService = accountService;
		this.timelineService = timelineService;
		this.memberService = memberService;
		this.walletService = walletService;
		
	}
	
	@ModelAttribute("defaultMinBalance")
	public long defaultMinBalance() {
		return Product.DEFAULT_MIN_BALANCE;
	}
	
	@ModelAttribute("wallet")
	public Wallet getMyWallet(Member member) {
		return walletService.getMyWallet(member);
	}
	
	@GetMapping
	public String index(Member member, AccountCommand accountCommand, Model model) {
		if(!accountService.findInstallmentSavingAccountByMember(member).isPresent()) {
			accountCommand.setBalance(Product.DEFAULT_MIN_BALANCE);
			accountCommand.setProductType(ProductType.INSTALLMENT_SAVING);
			model.addAttribute("accountCommand", accountCommand);
			return "simulator/searchSavingProductsForm";
		}
		
		if(timelineService.availableRegulaPayment(member)) {
			// 적금 납입 & timeline 완료처리
			application.proceedRegularPayAndTimelineDone(member);
		}
		
		List<Account> accounts = accountService.findAllByMember(member);
		List<Account> savingAccounts = accounts.stream()
				.filter(p -> p.getContract().getProductType().equals(ProductType.INSTALLMENT_SAVING))
				.map(mapper -> {
					InterestCalculator calculator = new InterestCalculator(mapper);
					mapper.getProduct().setInterestBeforeTax(calculator.getInterestBeforeTax());
					return mapper;
				})
				.collect(Collectors.toList());
		
		List<Account> depositAccounts = accounts.stream()
				.filter(p -> p.getContract().getProductType().equals(ProductType.FIXED_DEPOSIT))
				.map(mapper -> {
					InterestCalculator calculator = new InterestCalculator(mapper);
					mapper.getProduct().setInterestBeforeTax(calculator.getInterestBeforeTax());
					return mapper;
				})
				.collect(Collectors.toList());
		model.addAttribute("savingAccounts", savingAccounts);
		model.addAttribute("depositAccounts", depositAccounts);
		model.addAttribute("accountCommand", accountCommand);
		model.addAttribute("member", member);
		return "simulator/index";
	}
	
	@PostMapping("/searchSavingProductsForm")
	public String searchSavingProductsForm(Member member, @ModelAttribute @Valid AccountCommand accountCommand, Errors errors, Model model) {
		if(errors.hasErrors()) {
			return "simulator/searchSavingProductsForm";
		}
		
		long balance = accountCommand.getBalance();
		Set<String> excludeCompanyNumbers = application.excludeCompaniesToSearchProducts(member, ProductType.INSTALLMENT_SAVING, balance);
		model.addAttribute("products", productService.getFilteredProductList(ProductType.INSTALLMENT_SAVING, balance, excludeCompanyNumbers));
		return "simulator/searchSavingProductsForm";
	}
	
	@PostMapping("/openAccount")
	public String openAccount(Member member, @ModelAttribute @Valid AccountCommand accountCommand, Errors errors, RedirectAttributes ra) {
		ProductType productType = accountCommand.getProductType();
		if(errors.hasErrors()) {
			return productType.equals(ProductType.INSTALLMENT_SAVING) ? "simulator/searchSavingProductsForm" : "simulator/searchDepositProductsForm";
		}
		
		long balance = accountCommand.getBalance();
		long productId = accountCommand.getProductId();
		if(productId == 0l) {
			throw new IllegalArgumentException("productId must be positive.");
		}
		Account account = new Account();
		Product product = productService.getFilteredProductById(productId, balance);
		account.setProduct(product);
		account.setMember(member);
		
		Contract contract = Account.writeUp(product, balance, member.getCurrent());
		account.setContract(contract);
		if(application.openAccount(account) != null) {
			ra.addFlashAttribute("message", "simulator.openAccountSuccess");
		}
		return "redirect:/simulator";
	}
	
	@GetMapping("/nextTurn")
	public String nextTurn(Member member, RedirectAttributes ra) {
		Timeline timeline = timelineService.next(member);
		memberService.changeCurrent(member, timeline.getActiveDate());
		ra.addFlashAttribute("message", "simulator.nextTurnSuccess");
		return "redirect:/simulator";
	}
	
	@GetMapping("/closeAccount")
	public String closeAccount(Member member, RedirectAttributes ra) {
		ra.addFlashAttribute("message", application.closeAccount(member) ? "simulator.closeAccountSuccess" : "simulator.closeAccountFail");
		return "redirect:/simulator";
	}
	
	@GetMapping("/searchDepositProductsForm")
	public String searchDepositProductsForm(Member member, AccountCommand accountCommand, Model model) {
		Wallet wallet = walletService.getMyWallet(member);
		if(wallet == null) {
			throw new NullPointerException("wallet is null");
		}
		long balance = wallet.getBalance() > Product.MAX_DEPOSIT_PROTECTION_BALANCE ? Product.MAX_DEPOSIT_PROTECTION_BALANCE : wallet.getBalance();
		accountCommand.setBalance(balance);
		accountCommand.setProductType(ProductType.FIXED_DEPOSIT);
		
		Set<String> excludeCompanyNumbers = application.excludeCompaniesToSearchProducts(member, ProductType.FIXED_DEPOSIT, balance);
		model.addAttribute("products", productService.getFilteredProductList(ProductType.FIXED_DEPOSIT, balance, excludeCompanyNumbers));
		model.addAttribute("accountCommand", accountCommand);
		return "simulator/searchDepositProductsForm";
	}
	
}
