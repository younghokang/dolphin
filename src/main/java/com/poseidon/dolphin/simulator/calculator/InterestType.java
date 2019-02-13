package com.poseidon.dolphin.simulator.calculator;

import java.util.Arrays;

import com.poseidon.dolphin.simulator.account.Contract;
import com.poseidon.dolphin.simulator.calculator.strategy.FixedDepositDailySimpleInterestStrategy;
import com.poseidon.dolphin.simulator.calculator.strategy.FixedDepositMonthlyCompoundInterestStrategy;
import com.poseidon.dolphin.simulator.calculator.strategy.FixedDepositMonthlySimpleInterestStrategy;
import com.poseidon.dolphin.simulator.calculator.strategy.InstallmentSavingDailySimpleInterestStrategy;
import com.poseidon.dolphin.simulator.calculator.strategy.InstallmentSavingMonthlyCompoundInterestStrategy;
import com.poseidon.dolphin.simulator.calculator.strategy.InstallmentSavingMonthlySimpleInterestStrategy;
import com.poseidon.dolphin.simulator.calculator.strategy.InterestStrategy;
import com.poseidon.dolphin.simulator.product.Interest;
import com.poseidon.dolphin.simulator.product.ProductType;

public enum InterestType {
	/**
	 * 적금 단리(일)
	 */
	INSTALLMENT_SAVING_DAYILY_SIMPLE(
		new InstallmentSavingDailySimpleInterestStrategy(), 
		ProductType.INSTALLMENT_SAVING, 
		Interest.DAY_SIMPLE),
	/**
	 * 적금 단리(월)
	 */
	INSTALLMENT_SAVING_MONTHLY_SIMPLE( 
		new InstallmentSavingMonthlySimpleInterestStrategy(), 
		ProductType.INSTALLMENT_SAVING, 
		Interest.MONTH_SIMPLE),
	/**
	 * 적금 복리(월)
	 */
	INSTALLMENT_SAVING_MONTHLY_COMPOUND(
		new InstallmentSavingMonthlyCompoundInterestStrategy(), 
		ProductType.INSTALLMENT_SAVING, 
		Interest.MONTH_COMPOUND),
	/**
	 * 예금 단리(일)
	 */
	FIXED_DEPOSIT_DAILY_SIMPLE(
		new FixedDepositDailySimpleInterestStrategy(), 
		ProductType.FIXED_DEPOSIT, 
		Interest.DAY_SIMPLE),
	/**
	 * 예금 단리(월)
	 */
	FIXED_DEPOSIT_MONTHLY_SIMPLE(
		new FixedDepositMonthlySimpleInterestStrategy(), 
		ProductType.FIXED_DEPOSIT, 
		Interest.MONTH_SIMPLE),
	/**
	 * 예금 복리(월)
	 */
	FIXED_DEPOSIT_MONTHLY_COMPOUND(
		new FixedDepositMonthlyCompoundInterestStrategy(), 
		ProductType.FIXED_DEPOSIT, 
		Interest.MONTH_COMPOUND);
	
	private final InterestStrategy interestStrategy;
	private final ProductType productType;
	private final Interest interest;
	InterestType(InterestStrategy interestStrategy, ProductType productType, Interest interest) {
		this.interestStrategy = interestStrategy;
		this.productType = productType;
		this.interest = interest;
	}
	
	public InterestStrategy getInterestStrategy() {
		return interestStrategy;
	}
	public ProductType getProductType() {
		return productType;
	}
	public Interest getInterest() {
		return interest;
	}

	public static InterestType findByContract(Contract contract) {
		return Arrays.stream(InterestType.values()).filter(interestType -> {
			return interestType.getProductType() == contract.getProductType() && 
					interestType.getInterest() == contract.getInterest(); 
		}).findAny()
		.orElseThrow(UnsupportedOperationException::new);
	}
	
}
