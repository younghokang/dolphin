package com.poseidon.dolphin.simulator.calculator;

import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.AccountDetail;
import com.poseidon.dolphin.simulator.account.Contract;
import com.poseidon.dolphin.simulator.calculator.strategy.InterestStrategy;
import com.poseidon.dolphin.simulator.product.ProductType;

public class InterestCalculator {
	private final Account account;
	private final InterestType interestType;
	private final InterestStrategy interestStrategy;
	
	public InterestCalculator(final Account account) {
		this.account = account;
		this.interestType = InterestType.findByContract(account.getContract());
		this.interestStrategy = interestType.getInterestStrategy();
	}
	
	public InterestType getInterestType() {
		return interestType;
	}
	public Account getAccount() {
		return account;
	}
	public InterestStrategy getInterestStrategy() {
		return interestStrategy;
	}
	
	public long getInterestBeforeTax() {
		return interestStrategy.getInterestBeforeTax(account);
	}
	public long getTotalPrincipal() {
		return calculateTotalPrincipal(account);
	}
	public long getInterestIncomeTax() {
		return Math.round(getInterestBeforeTax() * account.getContract().getTaxRate());
	}
	
	private long calculateTotalPrincipal(final Account account) {
		final Contract contract = account.getContract();
		if(contract.getProductType() == ProductType.INSTALLMENT_SAVING) {
			final long turns = contract.getPaymentFrequency().calculateTurns(contract.getContractDate(), contract.getExpiryDate());
			long totalPrincipal = contract.getBalance() * turns;
			if(!account.getAccountDetails().isEmpty()) {
				totalPrincipal = 0;
				for(final AccountDetail accountDetail : account.getAccountDetails()) {
					totalPrincipal += accountDetail.getBalance();
				}
			}
			return totalPrincipal;
		}
		return contract.getBalance();
	}
	
	
}
