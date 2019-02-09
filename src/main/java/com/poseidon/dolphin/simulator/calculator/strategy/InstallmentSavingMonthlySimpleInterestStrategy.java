package com.poseidon.dolphin.simulator.calculator.strategy;

import java.time.temporal.ChronoUnit;

import com.poseidon.dolphin.simulator.account.Account;

/**
 * @author gang-yeongho
 * 세전이자(월단리) ((예치금 X 총 운용 월적수 X 연이율) / 12)
 * 총 운용 월적수: ((계약월수 X (계약월수 + 1)) / 2)
 */
public class InstallmentSavingMonthlySimpleInterestStrategy implements InterestStrategy {

	@Override
	public long getInterestBeforeTax(Account account) {
		double interestRate = account.getContract().getInterestRate();
		long balance = account.getContract().getBalance();
		int numberOfMonthsOfContract = (int)ChronoUnit.MONTHS.between(account.getContract().getContractDate(), account.getContract().getExpiryDate());
		return calculateBeforeMonthlySimpleInterestTax(balance, interestRate, numberOfMonthsOfContract);
	}
	
	private long calculateBeforeMonthlySimpleInterestTax(long balance, double interestRate, int numberOfMonthsOfContract) {
		return Math.round(balance * calculateTotalMonthlyRevenue(numberOfMonthsOfContract) * interestRate / 12);
	}
	
	/**
	 * 총 운용 월적수: ((계약월수 X (계약월수 + 1)) / 2)
	 * @param numberOfMonthsOfContract 계약월수
	 * @return
	 */
	private int calculateTotalMonthlyRevenue(int numberOfMonthsOfContract) {
		return (numberOfMonthsOfContract * (numberOfMonthsOfContract + 1)) / 2;
	}

}
