package com.poseidon.dolphin.simulator.calculator.strategy;

import java.time.temporal.ChronoUnit;

import com.poseidon.dolphin.simulator.account.Account;

/**
 * @author gang-yeongho
 * 세전이자(월복리) (예치금 X (1 + 연이율 / 12) ^ (계약월수 X 12 / 12) - 예치금)
 */
public class FixedDepositMonthlyCompoundInterestStrategy implements InterestStrategy {

	@Override
	public long getInterestBeforeTax(Account account) {
		long balance = account.getContract().getBalance();
		double interestRate = account.getContract().getInterestRate();
		long numberOfMonthsOfContract = ChronoUnit.MONTHS.between(account.getContract().getContractDate(), account.getContract().getExpiryDate());
		return calculateBeforeMonthCompoundInterestTax(balance, interestRate, numberOfMonthsOfContract);
	}
	
	private long calculateBeforeMonthCompoundInterestTax(long balance, double interestRate, long numberOfMonthsOfContract) {
		return Math.round(balance * Math.pow(1 + interestRate / 12, numberOfMonthsOfContract * 12 / 12) - balance);
	}

}
