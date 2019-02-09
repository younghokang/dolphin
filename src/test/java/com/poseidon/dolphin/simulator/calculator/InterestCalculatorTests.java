package com.poseidon.dolphin.simulator.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.AccountDetail;
import com.poseidon.dolphin.simulator.account.Contract;
import com.poseidon.dolphin.simulator.account.PaymentFrequency;
import com.poseidon.dolphin.simulator.calculator.InterestCalculator;
import com.poseidon.dolphin.simulator.product.Interest;
import com.poseidon.dolphin.simulator.product.ProductType;

public class InterestCalculatorTests {
	
	@Test
	public void test_적금_일단리_통장내역기반_이자율산출과_예상치를_통한_만기예상액() {
		long balance = 1800000;
		double interestRate = 0.028;
		LocalDate contractDate = LocalDate.of(2018, 1, 1);
		LocalDate expiryDate = contractDate.plusMonths(24);
		double taxRate = 0.154;
		
		Contract contract = new Contract();
		contract.setProductType(ProductType.INSTALLMENT_SAVING);
		contract.setBalance(balance);
		contract.setInterest(Interest.DAY_SIMPLE);
		contract.setInterestRate(interestRate);
		contract.setContractDate(contractDate);
		contract.setExpiryDate(expiryDate);
		contract.setPaymentFrequency(PaymentFrequency.MONTH);
		contract.setTaxRate(taxRate);
		
		List<AccountDetail> details = makeDummy();
		Account account = new Account();
		account.setContract(contract);
		account.setAccountDetails(details);
		
		InterestCalculator calculator = new InterestCalculator(account);
		long beforeInterestTax = calculator.getInterestBeforeTax();
		assertThat(beforeInterestTax).isEqualTo(1262623);
		
		account.setAccountDetails(Collections.emptyList());
		
		calculator = new InterestCalculator(account);
		assertThat(calculator.getInterestBeforeTax()).isEqualTo(1262623);
	}
	
	private List<AccountDetail> makeDummy() {
		return Arrays.asList(
				new AccountDetail(1, 1800000, LocalDate.of(2018, 1, 1)),
				new AccountDetail(2, 1800000, LocalDate.of(2018, 2, 1)),
				new AccountDetail(3, 1800000, LocalDate.of(2018, 3, 1)),
				new AccountDetail(4, 1800000, LocalDate.of(2018, 4, 1)),
				new AccountDetail(5, 1800000, LocalDate.of(2018, 5, 1)),
				new AccountDetail(6, 1800000, LocalDate.of(2018, 6, 1)),
				new AccountDetail(7, 1800000, LocalDate.of(2018, 7, 1)),
				new AccountDetail(8, 1800000, LocalDate.of(2018, 8, 1)),
				new AccountDetail(9, 1800000, LocalDate.of(2018, 9, 1)),
				new AccountDetail(10, 1800000, LocalDate.of(2018, 10, 1)),
				new AccountDetail(11, 1800000, LocalDate.of(2018, 11, 1)),
				new AccountDetail(12, 1800000, LocalDate.of(2018, 12, 1)),
				new AccountDetail(13, 1800000, LocalDate.of(2019, 1, 1)),
				new AccountDetail(14, 1800000, LocalDate.of(2019, 2, 1)),
				new AccountDetail(15, 1800000, LocalDate.of(2019, 3, 1)),
				new AccountDetail(16, 1800000, LocalDate.of(2019, 4, 1)),
				new AccountDetail(17, 1800000, LocalDate.of(2019, 5, 1)),
				new AccountDetail(18, 1800000, LocalDate.of(2019, 6, 1)),
				new AccountDetail(19, 1800000, LocalDate.of(2019, 7, 1)),
				new AccountDetail(20, 1800000, LocalDate.of(2019, 8, 1)),
				new AccountDetail(21, 1800000, LocalDate.of(2019, 9, 1)),
				new AccountDetail(22, 1800000, LocalDate.of(2019, 10, 1)),
				new AccountDetail(23, 1800000, LocalDate.of(2019, 11, 1)),
				new AccountDetail(24, 1800000, LocalDate.of(2019, 12, 1))
				);
	}
	
	@Test
	public void test_적금_월단리_월복리() {
		long balance = 1800000;
		double interestRate = 0.028;
		LocalDate contractDate = LocalDate.of(2018, 1, 1);
		LocalDate expiryDate = contractDate.plusMonths(24);
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
		
		List<AccountDetail> details = Collections.emptyList();
		Account account = new Account();
		account.setContract(contract);
		account.setAccountDetails(details);
		
		InterestCalculator calculator = new InterestCalculator(account);
		long beforeInterestTax = calculator.getInterestBeforeTax();
		assertThat(beforeInterestTax).isEqualTo(1260000);
		
		contract.setInterest(Interest.MONTH_COMPOUND);
		account.setContract(contract);
		
		calculator = new InterestCalculator(account);
		beforeInterestTax = calculator.getInterestBeforeTax();
		assertThat(beforeInterestTax).isEqualTo(1282832);
	}
	
	@Test
	public void test_예금_일단리_월단리_월복리() {
		long balance = 50000000;
		double interestRate = 0.0255;
		LocalDate contractDate = LocalDate.of(2018, 1, 1);
		LocalDate expiryDate = contractDate.plusYears(2);
		double taxRate = 0.154;
		
		Contract contract = new Contract();
		contract.setProductType(ProductType.FIXED_DEPOSIT);
		contract.setBalance(balance);
		contract.setInterest(Interest.DAY_SIMPLE);
		contract.setInterestRate(interestRate);
		contract.setContractDate(contractDate);
		contract.setExpiryDate(expiryDate);
		contract.setPaymentFrequency(PaymentFrequency.MONTH);
		contract.setTaxRate(taxRate);
		
		Account account = new Account();
		account.setContract(contract);
		account.setAccountDetails(Collections.emptyList());
		
		InterestCalculator calculator = new InterestCalculator(account);
		long beforeInterestTax = calculator.getInterestBeforeTax();
		assertThat(beforeInterestTax).isEqualTo(2550000);
		
		contract.setInterest(Interest.MONTH_SIMPLE);
		account.setContract(contract);
		calculator = new InterestCalculator(account);
		assertThat(calculator.getInterestBeforeTax()).isEqualTo(2550000);
		
		contract.setInterest(Interest.MONTH_COMPOUND);
		account.setContract(contract);
		calculator = new InterestCalculator(account);
		assertThat(calculator.getInterestBeforeTax()).isEqualTo(2613298);
	}
	
	@Test
	public void test_원금합계_이자과세_과세후수령액() {
		long balance = 50000000;
		double interestRate = 0.025;
		LocalDate contractDate = LocalDate.of(2018, 1, 1);
		LocalDate expiryDate = contractDate.plusYears(1);
		double taxRate = 0.154;
		
		Contract contract = new Contract();
		contract.setProductType(ProductType.FIXED_DEPOSIT);
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
		
		InterestCalculator calculator = new InterestCalculator(account);
		long beforeInterestTax = calculator.getInterestBeforeTax();
		assertThat(beforeInterestTax).isEqualTo(1250000);
		
		long totalPrincipal = calculator.getTotalPrincipal();
		assertThat(totalPrincipal).isEqualTo(50000000);
		
		long interestIncomeTax = calculator.getInterestIncomeTax();
		assertThat(interestIncomeTax).isEqualTo(192500);
		
		long afterPayingTax = totalPrincipal + beforeInterestTax - interestIncomeTax;
		assertThat(afterPayingTax).isEqualTo(51057500);
	}
	
}
