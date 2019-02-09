package com.poseidon.dolphin.simulator.calculator.strategy;

import java.time.temporal.ChronoUnit;

import com.poseidon.dolphin.simulator.account.Account;

/**
 * @author gang-yeongho
 * 세전이자(월단리) (예치금 X (1 + 연이율 X 계약월수 / 12) - 예치금)
 */
public class FixedDepositMonthlySimpleInterestStrategy implements InterestStrategy {

	@Override
	public long getInterestBeforeTax(Account account) {
		long balance = account.getContract().getBalance();
		double interestRate = account.getContract().getInterestRate();
		long numberOfMonthsOfContract = ChronoUnit.MONTHS.between(account.getContract().getContractDate(), account.getContract().getExpiryDate());
		return calculateBeforeMonthlySimpleInterestTax(balance, interestRate, numberOfMonthsOfContract);
	}
	
	private long calculateBeforeMonthlySimpleInterestTax(long balance, double interestRate, long numberOfMonthsOfContract) {
		return Math.round(balance * (1 + interestRate * numberOfMonthsOfContract / 12) - balance);
	}

}
