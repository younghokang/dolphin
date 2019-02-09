package com.poseidon.dolphin.simulator.calculator.strategy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.AccountDetail;
import com.poseidon.dolphin.simulator.account.PaymentFrequency;

/**
 * @author gang-yeongho
 * 세전이자(일단리) (회차별 입금금액 X 약정금리 X 예치일수 / 계약일수) 를 모두 합산하여 지급
 */
public class InstallmentSavingDailySimpleInterestStrategy implements InterestStrategy {
	private static final int DAY_OF_YEAR = 365;

	@Override
	public long getInterestBeforeTax(Account account) {
		long balance = account.getContract().getBalance();
		double interestRate = account.getContract().getInterestRate();
		LocalDate contractDate = account.getContract().getContractDate();
		LocalDate expiryDate = account.getContract().getExpiryDate();
		PaymentFrequency paymentFrequency = account.getContract().getPaymentFrequency();
		long numberOfDaysOfContract = ChronoUnit.DAYS.between(contractDate, expiryDate);
		if(account.getAccountDetails().isEmpty()) {
			List<AccountDetail> accountDetails = new ArrayList<>();
			long numberOfTurn = paymentFrequency.calculateTurns(contractDate, expiryDate);
			LocalDate depositDate = contractDate;
			for(int i=1; i<=numberOfTurn; i++) {
				accountDetails.add(new AccountDetail(i, balance, depositDate));
				depositDate = paymentFrequency.nextTurn(depositDate);
			}
			return calculateWithAccountDetails(interestRate, numberOfDaysOfContract, Collections.unmodifiableList(accountDetails));
		}
		return calculateWithAccountDetails(interestRate, numberOfDaysOfContract, account.getAccountDetails());
	}
	
	private long calculateWithAccountDetails(final double interestRate, final long numberOfDaysOfContract, final List<AccountDetail> accountDetails) {
		LocalDate depositDate = LocalDate.MIN;
		long numberOfDaysOfDeposit = numberOfDaysOfContract;
		double sumOfInterest = 0;
		for(final AccountDetail accountDetail : accountDetails) {
			if(!depositDate.isEqual(LocalDate.MIN)) {
				numberOfDaysOfDeposit -= ChronoUnit.DAYS.between(depositDate, accountDetail.getDepositDate());
			}
			sumOfInterest += accountDetail.getBalance() * interestRate * numberOfDaysOfDeposit / DAY_OF_YEAR;
			depositDate = accountDetail.getDepositDate();
		}
		return (long)Math.floor(sumOfInterest);
	}

}
