package com.poseidon.dolphin.simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poseidon.dolphin.member.Member;
import com.poseidon.dolphin.member.service.MemberService;
import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.AccountCommand;
import com.poseidon.dolphin.simulator.account.Contract;
import com.poseidon.dolphin.simulator.account.service.AccountService;
import com.poseidon.dolphin.simulator.calculator.InterestCalculator;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductType;
import com.poseidon.dolphin.simulator.product.service.ProductService;
import com.poseidon.dolphin.simulator.timeline.Activity;
import com.poseidon.dolphin.simulator.timeline.Timeline;
import com.poseidon.dolphin.simulator.timeline.service.TimelineService;
import com.poseidon.dolphin.simulator.wallet.TransferType;
import com.poseidon.dolphin.simulator.wallet.Wallet;
import com.poseidon.dolphin.simulator.wallet.WalletLog;
import com.poseidon.dolphin.simulator.wallet.service.WalletService;

@Controller
@RequestMapping("/simulator")
public class SimulatorController {
	private final ProductService productService;
	private final AccountService accountService;
	private final TimelineService timelineService;
	private final MemberService memberService;
	private final WalletService walletService;
	
	@Autowired
	public SimulatorController(ProductService productService, 
			AccountService accountService, TimelineService timelineService,
			MemberService memberService, WalletService walletService) {
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
		Optional<Account> installmentSavingAccount = accountService.findInstallmentSavingAccountByMember(member);
		if(!installmentSavingAccount.isPresent()) {
			accountCommand.setBalance(Product.DEFAULT_MIN_BALANCE);
			accountCommand.setProductType(ProductType.INSTALLMENT_SAVING);
			model.addAttribute("accountCommand", accountCommand);
			return "simulator/searchSavingProductsForm";
		}
		
		if(timelineService.availableRegulaPayment(member)) {
			// 적금 납입 & timeline 완료처리
			installmentSavingAccount.ifPresent(account -> {
					if(accountService.regularyPay(account.getId(), member)) {
						timelineService.done(member);
					}
				});
		}
		
		model.addAttribute("savingAccounts", findAllAccountsByProductTypeWithBeforeInterestRate(member, ProductType.INSTALLMENT_SAVING));
		model.addAttribute("depositAccounts", findAllAccountsByProductTypeWithBeforeInterestRate(member, ProductType.FIXED_DEPOSIT));
		model.addAttribute("accountCommand", accountCommand);
		model.addAttribute("member", member);
		return "simulator/index";
	}
	
	private List<Account> findAllAccountsByProductTypeWithBeforeInterestRate(Member member, ProductType productType) {
		return accountService.findAllByMember(member).stream()
			.filter(p -> p.getContract().getProductType().equals(productType))
			.map(mapper -> {
				InterestCalculator calculator = new InterestCalculator(mapper);
				mapper.getProduct().setInterestBeforeTax(calculator.getInterestBeforeTax());
				return mapper;
			})
			.collect(Collectors.toList());
	}
	
	@PostMapping("/searchSavingProductsForm")
	public String searchSavingProductsForm(Member member, @ModelAttribute @Valid AccountCommand accountCommand, Errors errors, Model model) {
		if(errors.hasErrors()) {
			return "simulator/searchSavingProductsForm";
		}
		
		long balance = accountCommand.getBalance();
		Set<String> excludeCompanyNumbers = excludeCompaniesToSearchProducts(member, ProductType.INSTALLMENT_SAVING, balance);
		model.addAttribute("products", productService.getFilteredProductList(ProductType.INSTALLMENT_SAVING, balance, excludeCompanyNumbers));
		return "simulator/searchSavingProductsForm";
	}
	
	private Set<String> excludeCompaniesToSearchProducts(Member member, ProductType productType, long balance) {
		Map<String, Long> groupingByCompanyWithBalance = accountService.findAllByMember(member).stream()
			.collect(Collectors.groupingBy(acc -> acc.getProduct().getCompanyNumber(), Collectors.summingLong(m -> {
				if(m.getContract().getProductType().equals(ProductType.FIXED_DEPOSIT)) {
					return m.getContract().getBalance();
				}
				
				long turns = m.getContract().getPaymentFrequency().calculateTurns(m.getContract().getContractDate(), m.getContract().getExpiryDate());
				return m.getContract().getBalance() * turns;
			})));
		return groupingByCompanyWithBalance.entrySet().stream()
				.filter(p -> {
					if(productType.equals(ProductType.FIXED_DEPOSIT)) {
						return p.getValue() + balance > Product.MAX_DEPOSIT_PROTECTION_BALANCE;
					}
					return p.getValue() + (balance * Product.DEFAULT_PERIOD) > Product.MAX_DEPOSIT_PROTECTION_BALANCE;
				})
				.map(mapper -> mapper.getKey())
				.collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
	}
	
	@PostMapping("/openAccount")
	public String openAccount(Member member, @ModelAttribute @Valid AccountCommand accountCommand, Errors errors, RedirectAttributes ra) {
		ProductType productType = accountCommand.getProductType();
		long balance = accountCommand.getBalance();
		long productId = accountCommand.getProductId();
		if(productId == 0l) {
			errors.rejectValue("productId", "must be positive");
		}
		
		if(errors.hasErrors()) {
			return productType.equals(ProductType.INSTALLMENT_SAVING) ? "simulator/searchSavingProductsForm" : "simulator/searchDepositProductsForm";
		}
		
		Product product = productService.getFilteredProductById(productId, balance);
		Contract contract = Account.writeUp(product, balance, member.getCurrent());
		
		Account account = accountService.openAccount(member, product, contract);
		timelineService.saveAll(makeTimelines(account));
		if(productType.equals(ProductType.FIXED_DEPOSIT)) {
			TransferType transferType = TransferType.FIXED_DEPOSIT_OPEN;
			long walletBalance = transferType.calculateBalance(balance);
			walletService.save(member, walletBalance, new WalletLog(walletBalance, account.getLastModifiedDate(), TransferType.FIXED_DEPOSIT_OPEN, account.getId()));
		}
		
		ra.addFlashAttribute("message", "simulator.openAccountSuccess");
		return "redirect:/simulator";
	}
	
	private List<Timeline> makeTimelines(Account savedAccount) {
		List<Timeline> timelines = new ArrayList<>();
		switch(savedAccount.getContract().getProductType()) {
		case INSTALLMENT_SAVING:
			timelines.addAll(Timeline.makeTimelinesByInstallmentSaving(savedAccount));
			break;
		case FIXED_DEPOSIT:
			timelines.addAll(Timeline.makeTimelinesByFixedDeposit(savedAccount));
			break;
		}
		return timelines;
	}
	
	@GetMapping("/nextTurn")
	public String nextTurn(Member member, RedirectAttributes ra) {
		Timeline timeline = timelineService.next(member);
		member.setCurrent(timeline.getActiveDate());
		memberService.saveChanges(member);
		ra.addFlashAttribute("message", "simulator.nextTurnSuccess");
		return "redirect:/simulator";
	}
	
	@GetMapping("/closeAccount")
	public String closeAccount(Member member, RedirectAttributes ra) {
		String message = "simulator.closeAccountFail";
		Timeline timeline = timelineService.next(member);
		if(timeline != null) {
			if(availableActivity(timeline)) {
				Timeline doneTimeline = timelineService.done(member);
				Account account = accountService.closeAccount(doneTimeline.getReferenceId());
				InterestCalculator calculator = new InterestCalculator(account);
				long balance = calculator.getTotalPrincipal() + calculator.getInterestBeforeTax() - calculator.getInterestIncomeTax();
				
				WalletLog walletLog = new WalletLog(balance, account.getLastModifiedDate(), TransferType.findByActivity(timeline.getActivity()), account.getId());
				walletService.save(member, balance, walletLog);
				message = "simulator.closeAccountSuccess";
			}
		}
		
		ra.addFlashAttribute("message", message);
		return "redirect:/simulator";
	}
	
	private boolean availableActivity(Timeline timeline) {
		return timeline.getActivity().equals(Activity.FIXED_DEPOSIT_ACCOUNT_CLOSE) || timeline.getActivity().equals(Activity.INSTALLMENT_SAVING_ACCOUNT_CLOSE);
	}
	
	@GetMapping("/searchDepositProductsForm")
	public String searchDepositProductsForm(Member member, AccountCommand accountCommand, Model model) {
		Wallet wallet = walletService.getMyWallet(member);
		Assert.notNull(wallet, "wallet must not be null");
		
		long balance = wallet.getBalance() > Product.MAX_DEPOSIT_PROTECTION_BALANCE ? Product.MAX_DEPOSIT_PROTECTION_BALANCE : wallet.getBalance();
		accountCommand.setBalance(balance);
		accountCommand.setProductType(ProductType.FIXED_DEPOSIT);
		
		Set<String> excludeCompanyNumbers = excludeCompaniesToSearchProducts(member, ProductType.FIXED_DEPOSIT, balance);
		model.addAttribute("products", productService.getFilteredProductList(ProductType.FIXED_DEPOSIT, balance, excludeCompanyNumbers));
		model.addAttribute("accountCommand", accountCommand);
		return "simulator/searchDepositProductsForm";
	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView simulatorErrorPage(Exception exception) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", exception);
		mav.setViewName("simulator/error");
		return mav;
	}
	
}
