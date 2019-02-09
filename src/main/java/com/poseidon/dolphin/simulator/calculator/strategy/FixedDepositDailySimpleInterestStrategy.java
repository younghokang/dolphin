package com.poseidon.dolphin.simulator.calculator.strategy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.AccountDetail;

/**
 * @author gang-yeongho
 * 세전이자(일단리) (가입금액 X 약정금리 X 예치일수 / 계약일수)
 */
public class FixedDepositDailySimpleInterestStrategy implements InterestStrategy {
	private static final int DAY_OF_YEAR = 365;

	@Override
	public long getInterestBeforeTax(Account account) {
		long balance = account.getContract().getBalance();
		double interestRate = account.getContract().getInterestRate();
		long numberOfDaysOfContract = ChronoUnit.DAYS.between(account.getContract().getContractDate(), account.getContract().getExpiryDate());
		long numberOfDaysOfDeposit = numberOfDaysOfContract;
		if(!account.getAccountDetails().isEmpty()) {
			balance = 0;
			numberOfDaysOfDeposit = 0;
			LocalDate depositDate = account.getContract().getContractDate();
			for(AccountDetail detail : account.getAccountDetails()) {
				balance += detail.getBalance();
				if(!depositDate.isEqual(detail.getDepositDate())) {
					numberOfDaysOfDeposit += ChronoUnit.DAYS.between(depositDate, detail.getDepositDate());
				}
				depositDate = detail.getDepositDate();
			}
		}
		return calculateBeforeDailyInterestTax(balance, interestRate, numberOfDaysOfDeposit);
	}
	
	private long calculateBeforeDailyInterestTax(long balance, double interestRate, long numberOfDaysOfDeposit) {
		return Math.round(balance * interestRate * numberOfDaysOfDeposit / DAY_OF_YEAR);
	}

}
