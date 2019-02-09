package com.poseidon.dolphin.simulator.calculator.strategy;

import java.time.temporal.ChronoUnit;

import com.poseidon.dolphin.simulator.account.Account;

/**
 * @author gang-yeongho
 * 세전이자(월복리) ((예치금 X (1+연이율/12) X (1+연이율/12)^계약월수 - 1 / (연이율/12) - 원금합계)
 */
public class InstallmentSavingMonthlyCompoundInterestStrategy implements InterestStrategy {

	@Override
	public long getInterestBeforeTax(Account account) {
		double interestRate = account.getContract().getInterestRate();
		long balance = account.getContract().getBalance();
		int numberOfMonthsOfContract = (int)ChronoUnit.MONTHS.between(account.getContract().getContractDate(), account.getContract().getExpiryDate());
		return calculateBeforeMonthlyCompoundInterestTax(balance, interestRate, numberOfMonthsOfContract);
	}
	
	private long calculateBeforeMonthlyCompoundInterestTax(long balance, double interestRate, int numberOfMonthsOfContract) {
		return Math.round(Math.round(balance * (1 + interestRate / 12)) * (Math.pow(1 + interestRate / 12, numberOfMonthsOfContract) - 1) / (interestRate / 12) - (balance * numberOfMonthsOfContract));
	}

}
