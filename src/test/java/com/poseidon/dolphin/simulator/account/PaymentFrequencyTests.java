package com.poseidon.dolphin.simulator.account;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import com.poseidon.dolphin.simulator.account.PaymentFrequency;

public class PaymentFrequencyTests {
	private LocalDate contractDate;
	private LocalDate expiryDate;
	
	@Before
	public void setUp() {
		contractDate = LocalDate.of(2019, 1, 1);
		expiryDate = contractDate.plusYears(1);
	}
	
	@Test
	public void test_납입주기_단위별_총회차수_다음회차일() {
		assertThat(PaymentFrequency.DAY.calculateTurns(contractDate, expiryDate)).isEqualTo(365);
		assertThat(PaymentFrequency.DAY.nextTurn(contractDate)).isEqualTo(LocalDate.of(2019, 1, 2));
		
		assertThat(PaymentFrequency.WEEK.calculateTurns(contractDate, expiryDate)).isEqualTo(52);
		assertThat(PaymentFrequency.WEEK.nextTurn(contractDate)).isEqualTo(LocalDate.of(2019, 1, 8));
		
		assertThat(PaymentFrequency.MONTH.calculateTurns(contractDate, expiryDate)).isEqualTo(12);
		assertThat(PaymentFrequency.MONTH.nextTurn(contractDate)).isEqualTo(LocalDate.of(2019, 2, 1));
		
		assertThat(PaymentFrequency.YEAR.calculateTurns(contractDate, expiryDate)).isEqualTo(1);
		assertThat(PaymentFrequency.YEAR.nextTurn(contractDate)).isEqualTo(LocalDate.of(2020, 1, 1));
	}
	
}
