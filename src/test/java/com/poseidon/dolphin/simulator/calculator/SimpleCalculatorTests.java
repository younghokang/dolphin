package com.poseidon.dolphin.simulator.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.Test;

import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.Contract;
import com.poseidon.dolphin.simulator.account.PaymentFrequency;
import com.poseidon.dolphin.simulator.calculator.CalculatorResult;
import com.poseidon.dolphin.simulator.calculator.InterestCalculator;
import com.poseidon.dolphin.simulator.calculator.SimpleCalculator;
import com.poseidon.dolphin.simulator.product.Interest;
import com.poseidon.dolphin.simulator.product.ProductType;

public class SimpleCalculatorTests {
	
	@Test
	public void callSimpleCalculator() {
		long balance = 1800000;
		double interestRate = 0.028;
		int period = 12;
		LocalDate contractDate = LocalDate.of(2018, 1, 1);
		LocalDate expiryDate = contractDate.plusMonths(period);
		double taxRate = 0.154;
		
		Contract contract = new Contract();
		contract.setProductType(ProductType.INSTALLMENT_SAVING);
		contract.setBalance(balance);
		contract.setInterest(Interest.MONTH_SIMPLE);
		contract.setInterestRate(interestRate);
		contract.setContractDate(contractDate);
		contract.setExpiryDate(expiryDate);
		contract.setPaymentFrequency(PaymentFrequency.MONTH);
		contract.setTaxRate(taxRate);
		
		Account account = new Account();
		account.setContract(contract);
		account.setAccountDetails(Collections.emptyList());
		
		InterestCalculator interestCalculator = new InterestCalculator(account);
		long beforeInterestTax = interestCalculator.getInterestBeforeTax();
		assertThat(beforeInterestTax).isEqualTo(327600);
		
		interestCalculator.getTotalPrincipal();
		interestCalculator.getInterestIncomeTax();
		
		SimpleCalculator calculator = new SimpleCalculator(balance, interestRate, ProductType.INSTALLMENT_SAVING);
		calculator.setPeriod(period);
		calculator.setInterest(Interest.MONTH_SIMPLE);
		calculator.setPaymentFrequency(PaymentFrequency.MONTH);
		calculator.setTaxRate(taxRate);
		CalculatorResult result = calculator.calculate();
		assertThat(result.getInterestBeforeTax()).isEqualTo(327600);
	}

}
