package com.poseidon.dolphin.simulator.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import com.poseidon.dolphin.member.Member;
import com.poseidon.dolphin.simulator.product.InterestRate;
import com.poseidon.dolphin.simulator.product.InterestRateType;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductOption;
import com.poseidon.dolphin.simulator.product.ProductType;
import com.poseidon.dolphin.simulator.product.ReserveType;
import com.poseidon.dolphin.simulator.product.TaxType;

public class AccountTests {
	
	@Test
	public void givenAccountDetailsThenReturnNext() {
		int dateOfPayment = 25;
		long balance = 1800000;
		LocalDate depositDate = LocalDate.of(2019, 1, dateOfPayment);
		Account account = new Account();
		
		Contract contract = new Contract();
		contract.setDateOfPayment(dateOfPayment);
		account.setContract(contract);
		
		AccountDetail accountDetail = new AccountDetail();
		int turn = 1;
		accountDetail.setTurn(turn);
		accountDetail.setBalance(balance);
		accountDetail.setDepositDate(depositDate);
		account.getAccountDetails().add(accountDetail);
		
		accountDetail = new AccountDetail();
		accountDetail.setTurn(++turn);
		accountDetail.setBalance(balance);
		accountDetail.setDepositDate(depositDate.plusMonths(1));
		account.getAccountDetails().add(accountDetail);
		assertThat(account.getAccountDetails().size()).isEqualTo(2);
		
		AccountDetail lastAccountDetail = account.getAccountDetails().stream()
				.max(Comparator.comparing(AccountDetail::getTurn))
				.orElse(null);
		assertThat(lastAccountDetail).isNotNull();
		assertThat(lastAccountDetail).matches(p -> p.getTurn() == 2, "turn is not equal 2");
		assertThat(lastAccountDetail).matches(p -> p.getDepositDate().isEqual(depositDate.plusMonths(1)), "depositDate is not equal 2019-02-25");
		
		AccountDetail nextAccountDetail = nextAccountDetail(lastAccountDetail, dateOfPayment);
		assertThat(nextAccountDetail).matches(p -> p.getTurn() == 3, "turn is not equal 3");
		assertThat(nextAccountDetail).matches(p -> p.getDepositDate().isEqual(LocalDate.of(2019, 3, dateOfPayment)), "depositDate is not equal to 2019-03-25");
	}
	
	private AccountDetail nextAccountDetail(AccountDetail ad, int dateOfPayment) {
		AccountDetail accountDetail = new AccountDetail();
		accountDetail.setTurn(ad.getTurn() + 1);
		accountDetail.setDepositDate(nextDate(dateOfPayment, ad.getDepositDate()));
		return accountDetail;
	}
	
	private LocalDate nextDate(int dateOfPayment, LocalDate current) {
		LocalDate next = current.plusMonths(1);
		int dayOfMonth = next.lengthOfMonth() < dateOfPayment ? next.lengthOfMonth() : dateOfPayment;
		next = next.withDayOfMonth(dayOfMonth);
		return next;
	}
	
	@Test
	public void nextAccountDetailFunctionTest() {
		int dateOfPayment = 25;
		long balance = 1800000;
		LocalDate depositDate = LocalDate.of(2019, 1, dateOfPayment);
		Account account = new Account();
		
		Contract contract = new Contract();
		contract.setDateOfPayment(dateOfPayment);
		contract.setContractDate(depositDate);
		contract.setExpiryDate(depositDate.plusMonths(12));
		contract.setPaymentFrequency(PaymentFrequency.MONTH);
		account.setContract(contract);
		
		assertThat(account.nextAccountDetail()).isNull();
		
		AccountDetail accountDetail = new AccountDetail();
		int turn = 1;
		accountDetail.setTurn(turn);
		accountDetail.setBalance(balance);
		accountDetail.setDepositDate(depositDate);
		account.getAccountDetails().add(accountDetail);
		
		accountDetail = new AccountDetail();
		accountDetail.setTurn(++turn);
		accountDetail.setBalance(balance);
		accountDetail.setDepositDate(depositDate.plusMonths(1));
		account.getAccountDetails().add(accountDetail);
		assertThat(account.getAccountDetails().size()).isEqualTo(2);
		
		assertThat(account.nextAccountDetail()).isNotNull();
		assertThat(account.nextAccountDetail()).matches(p -> p.getTurn() == 3, "turn is not equal 3");
		assertThat(account.nextAccountDetail()).matches(p -> p.getDepositDate().isEqual(LocalDate.of(2019, 3, dateOfPayment)), "depositDate is not equal 2019-03-25");
	}
	
	@Test
	public void whenWriteUpThenReturnNewContract() {
		ProductType productType = ProductType.INSTALLMENT_SAVING;
		long balance = 1800000;
		LocalDate current = LocalDate.of(2019, 1, 1);
		int dateOfPayment = 25;
		int period = 12;
		ChronoUnit periodUnit = ChronoUnit.MONTHS;
		PaymentFrequency paymentFrequency = PaymentFrequency.MONTH;
		TaxType taxType = TaxType.GENERAL;
		double taxRate = TaxType.GENERAL.getTaxRate();
		
		Product product = new Product();
		product.setProductType(productType);
		product.setMinBalance(0);
		product.setMaxBalance(3000000);
		product.setMinPeriod(6);
		product.setMaxPeriod(12);
		product.setPeriodUnit(periodUnit);
		
		try {
			Account.writeUp(product, balance, current, dateOfPayment, period, periodUnit, paymentFrequency, taxType, taxRate);
			fail("filteredOptionId 가 존재하지않습니다.");
		} catch(NullPointerException e) {}
		
		product.setFilteredOptionId(1L);
		
		ProductOption productOption = new ProductOption();
		productOption.setId(1L);
		productOption.setInterestRateType(InterestRateType.SIMPLE);
		productOption.setReserveType(ReserveType.FIXED);
		product.getProductOptions().add(productOption);
		
		try {
			Account.writeUp(product, balance, current, dateOfPayment, period, periodUnit, paymentFrequency, taxType, taxRate);
			fail("36개월 상품이 없습니다.");
		} catch(NullPointerException e) {}
		
		productOption.setInterestRates(
				Arrays.asList(
						new InterestRate(6, 0.015, 0.017), 
						new InterestRate(12, 0.025, 0.027), 
						new InterestRate(24, 0.028, 0.030)));
		
		Contract contract = Account.writeUp(product, balance, current, dateOfPayment, period, periodUnit, paymentFrequency, taxType, taxRate);
		assertThat(contract).isNotNull();
	}
	
	@Test
	public void whenCallGetTotalAmountOfBalanceThenReturnLong() {
		Account account = new Account();
		List<AccountDetail> accountDetails = Arrays.asList(
				new AccountDetail(1, 5000000, LocalDate.now()),
				new AccountDetail(2, 5000000, LocalDate.now()),
				new AccountDetail(3, 5000000, LocalDate.now()));
		account.getAccountDetails().addAll(accountDetails);
		assertThat(account.getTotalAmountOfBalance()).isEqualTo(15000000);
		
		account.setAccountDetails(Collections.emptyList());
		assertThat(account.getTotalAmountOfBalance()).isZero();
	}
	
	@Test
	public void whenCallGetDayOfExpiryFromCurrentThenReturnLong() {
		Account account = new Account();
		
		Contract contract = new Contract();
		contract.setExpiryDate(LocalDate.of(2019, 1, 30));
		account.setContract(contract);
		
		Member member = new Member();
		member.setCurrent(LocalDate.of(2019, 1, 15));
		account.setMember(member);
		
		assertThat(account.getDayOfExpiryFromCurrent()).isEqualTo(15);
	}
	
	@Test
	public void givenCurrentDateAndContractDateAndExpireDateThenPercent() {
		Account account = new Account();
		
		Contract contract = new Contract();
		contract.setContractDate(LocalDate.of(2019, 1, 1));
		contract.setExpiryDate(LocalDate.of(2019, 1, 30));
		account.setContract(contract);
		
		Member member = new Member();
		member.setCurrent(LocalDate.of(2019, 1, 15));
		account.setMember(member);
		
		assertThat(account.getDayOfCurrentFromContractDate()).isEqualTo(14);
		assertThat(account.getDayOfContract()).isEqualTo(29);
		assertThat(account.getNumberOfPercentToExpiry()).isEqualTo(48);
	}
	
}
