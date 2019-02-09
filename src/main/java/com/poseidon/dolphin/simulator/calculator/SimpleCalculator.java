package com.poseidon.dolphin.simulator.calculator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.Contract;
import com.poseidon.dolphin.simulator.account.PaymentFrequency;
import com.poseidon.dolphin.simulator.product.Interest;
import com.poseidon.dolphin.simulator.product.ProductType;

public class SimpleCalculator {
	private final long balance;
	private final double interestRate;
	private final ProductType productType;
	private int period = 12;
	private ChronoUnit chronoUnit = ChronoUnit.MONTHS;
	private Interest interest = Interest.MONTH_SIMPLE;
	private PaymentFrequency paymentFrequency = PaymentFrequency.MONTH;
	private double taxRate = 0.154;
	private Long optionId;
	
	public SimpleCalculator(long balance, double interestRate, ProductType productType) {
		this.balance = balance;
		this.interestRate = interestRate;
		this.productType = productType;
	}

	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	public ChronoUnit getChronoUnit() {
		return chronoUnit;
	}
	public void setChronoUnit(ChronoUnit chronoUnit) {
		this.chronoUnit = chronoUnit;
	}
	public Interest getInterest() {
		return interest;
	}
	public void setInterest(Interest interest) {
		this.interest = interest;
	}
	public PaymentFrequency getPaymentFrequency() {
		return paymentFrequency;
	}
	public void setPaymentFrequency(PaymentFrequency paymentFrequency) {
		this.paymentFrequency = paymentFrequency;
	}
	public double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}
	public long getBalance() {
		return balance;
	}
	public double getInterestRate() {
		return interestRate;
	}
	public ProductType getProductType() {
		return productType;
	}
	public Long getOptionId() {
		return optionId;
	}
	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}

	public CalculatorResult calculate() {
		LocalDate contractDate = LocalDate.now();
		LocalDate expiryDate = contractDate.plus(getPeriod(), getChronoUnit());
		
		Contract contract = new Contract();
		contract.setProductType(getProductType());
		contract.setBalance(getBalance());
		contract.setInterest(getInterest());
		contract.setInterestRate(getInterestRate());
		contract.setContractDate(contractDate);
		contract.setExpiryDate(expiryDate);
		contract.setPaymentFrequency(getPaymentFrequency());
		contract.setTaxRate(getTaxRate());
		
		Account account = new Account();
		account.setContract(contract);
		account.setAccountDetails(Collections.emptyList());
		
		long optionId = getOptionId() == null ? 0 : getOptionId();
		
		final InterestCalculator interestCalculator = new InterestCalculator(account);
		return new CalculatorResult() {
			@Override
			public long getOptionId() {
				return optionId;
			}
			
			@Override
			public long getTotalPrincipal() {
				return interestCalculator.getTotalPrincipal();
			}
			
			@Override
			public long getInterestIncomeTax() {
				return interestCalculator.getInterestIncomeTax();
			}
			
			@Override
			public long getInterestBeforeTax() {
				return interestCalculator.getInterestBeforeTax();
			}
		};
	}
	
}
