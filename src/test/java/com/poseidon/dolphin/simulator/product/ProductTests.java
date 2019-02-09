package com.poseidon.dolphin.simulator.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;

public class ProductTests {
	
	@Test
	public void givenDayOfContractPeriodThenReturnInterestRate() {
		Product product = new Product();
		ProductOption productOption = new ProductOption();
		List<InterestRate> interestRates = Arrays.asList(
				new InterestRate(30, 0.013, 0.015),
				new InterestRate(90, 0.0145, 0.0165),
				new InterestRate(180, 0.0165, 0.0185),
				new InterestRate(365, 0.02, 0.022));
		productOption.setInterestRates(interestRates);
		product.getProductOptions().forEach(option -> {
			try {
				option.applyWithDayOfContractPeriod(29);
				fail();
			} catch(NoSuchElementException e) {
			}
			
			assertThat(option.applyWithDayOfContractPeriod(30).getRate()).isEqualTo(0.013);
			assertThat(option.applyWithDayOfContractPeriod(60).getRate()).isEqualTo(0.013);
			assertThat(option.applyWithDayOfContractPeriod(89).getRate()).isEqualTo(0.013);
			assertThat(option.applyWithDayOfContractPeriod(90).getRate()).isEqualTo(0.0145);
			assertThat(option.applyWithDayOfContractPeriod(91).getRate()).isEqualTo(0.0145);
			assertThat(option.applyWithDayOfContractPeriod(179).getRate()).isEqualTo(0.0145);
			assertThat(option.applyWithDayOfContractPeriod(180).getRate()).isEqualTo(0.0165);
			assertThat(option.applyWithDayOfContractPeriod(181).getRate()).isEqualTo(0.0165);
			assertThat(option.applyWithDayOfContractPeriod(364).getRate()).isEqualTo(0.0165);
			assertThat(option.applyWithDayOfContractPeriod(365).getRate()).isEqualTo(0.02);
			assertThat(option.applyWithDayOfContractPeriod(366).getRate()).isEqualTo(0.02);
		});
	}
	
	@Test
	public void givenPeriodThenReturnBoolean() {
		Product product = new Product();
		product.setMinPeriod(31);
		product.setMaxPeriod(365);
		assertThat(product.availablePeriod(30)).isFalse();
		assertThat(product.availablePeriod(31)).isTrue();
		assertThat(product.availablePeriod(32)).isTrue();
		assertThat(product.availablePeriod(364)).isTrue();
		assertThat(product.availablePeriod(365)).isTrue();
		assertThat(product.availablePeriod(366)).isFalse();
	}
	
	@Test
	public void givenBalanceThenReturnBoolean() {
		Product product = new Product();
		product.setMinBalance(100000L);
		product.setMaxBalance(3000000L);
		assertThat(product.availableBalance(99999)).isFalse();
		assertThat(product.availableBalance(100000)).isTrue();
		assertThat(product.availableBalance(100001)).isTrue();
		assertThat(product.availableBalance(2999999)).isTrue();
		assertThat(product.availableBalance(3000000)).isTrue();
		assertThat(product.availableBalance(3000001)).isFalse();
	}
	
	@Test
	public void givenAgeThenReturnBoolean() {
		Product product = new Product();
		product.setMinAge(17);
		product.setMaxAge(30);;
		assertThat(product.availableAge(16)).isFalse();
		assertThat(product.availableAge(17)).isTrue();
		assertThat(product.availableAge(30)).isTrue();
		assertThat(product.availableAge(31)).isFalse();
	}
	
}
