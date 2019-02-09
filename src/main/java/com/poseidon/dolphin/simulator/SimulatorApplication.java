package com.poseidon.dolphin.simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.service.AccountService;
import com.poseidon.dolphin.simulator.calculator.InterestCalculator;
import com.poseidon.dolphin.simulator.member.Member;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductType;
import com.poseidon.dolphin.simulator.timeline.Activity;
import com.poseidon.dolphin.simulator.timeline.Timeline;
import com.poseidon.dolphin.simulator.timeline.service.TimelineService;
import com.poseidon.dolphin.simulator.wallet.TransferType;
import com.poseidon.dolphin.simulator.wallet.WalletLog;
import com.poseidon.dolphin.simulator.wallet.service.WalletService;

@Component
public class SimulatorApplication {
	private final TimelineService timelineService;
	private final AccountService accountService;
	private final WalletService walletService;
	
	@Autowired
	public SimulatorApplication(TimelineService timelineService, AccountService accountService, WalletService walletService) {
		this.timelineService = timelineService;
		this.accountService = accountService;
		this.walletService = walletService;
	}
	
	public boolean closeAccount(Member member) {
		assert member != null;
		Timeline timeline = timelineService.next(member);
		if(timeline != null) {
			if(availableActivity(timeline)) {
				Timeline doneTimeline = timelineService.done(member);
				Account account = accountService.closeAccount(doneTimeline.getReferenceId());
				InterestCalculator calculator = new InterestCalculator(account);
				long balance = calculator.getTotalPrincipal() + calculator.getInterestBeforeTax() - calculator.getInterestIncomeTax();
				
				WalletLog walletLog = new WalletLog(balance, account.getLastModifiedDate(), TransferType.findByActivity(timeline.getActivity()), account.getId());
				return walletService.save(member, balance, walletLog) != null;
			}
		}
		return false;
	}
	
	private boolean availableActivity(Timeline timeline) {
		return timeline.getActivity().equals(Activity.FIXED_DEPOSIT_ACCOUNT_CLOSE) || timeline.getActivity().equals(Activity.INSTALLMENT_SAVING_ACCOUNT_CLOSE);
	}

	public Account openAccount(Account account) {
		Account savedAccount = accountService.openAccount(account.getMember(), account.getProduct(), account.getContract());
		timelineService.saveAll(makeTimelines(savedAccount));
		if(savedAccount.getContract().getProductType().equals(ProductType.FIXED_DEPOSIT)) {
			long balance = account.getContract().getBalance() * -1;
			walletService.save(account.getMember(), balance, new WalletLog(balance, savedAccount.getLastModifiedDate(), TransferType.FIXED_DEPOSIT_OPEN, savedAccount.getId()));
		}
		return savedAccount;
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
	
	public boolean proceedRegularPayAndTimelineDone(Member member) {
		return accountService.findInstallmentSavingAccountByMember(member).map(account -> {
			if(accountService.regularyPay(account.getId(), member)) {
				timelineService.done(member);
				return true;
			}
			return false;
		}).orElseThrow(IllegalArgumentException::new);
	}
	
	public Set<String> excludeCompaniesToSearchProducts(Member member, ProductType productType, long balance) {
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
	
}
