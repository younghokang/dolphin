package com.poseidon.dolphin.simulator.filter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.poseidon.dolphin.simulator.product.InterestRate;
import com.poseidon.dolphin.simulator.product.InterestRateType;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductOption;
import com.poseidon.dolphin.simulator.product.ProductType;
import com.poseidon.dolphin.simulator.product.ReserveType;

public class ProductFilterTests {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Test
	public void givenDepositProductsThenReturnFilteredProductList() {
		List<Product> products = createDummyDepositProducts();
		
		long balance = 50000000;
		
		assertThat(products.size()).isEqualTo(5);
		assertThat(products.get(0)).matches(p -> p.getCode().equals("HK00001") && p.getCompanyNumber().equals("0010345"));
		assertThat(products.get(1)).matches(p -> p.getCode().equals("24000") && p.getCompanyNumber().equals("0010346"));
		assertThat(products.get(2)).matches(p -> p.getCode().equals("24001") && p.getCompanyNumber().equals("0010346"));
		assertThat(products.get(3)).matches(p -> p.getCode().equals("2400") && p.getCompanyNumber().equals("0010349"));
		assertThat(products.get(4)).matches(p -> p.getCode().equals("2407") && p.getCompanyNumber().equals("0010349"));
		
		products.stream().forEach(item -> {
			log.debug(String.format("은행코드: %s, 상품코드: %s, 은행명: %s, 상품명: %s, 가입한도: %,d원", item.getCompanyNumber(), item.getCode(), item.getCompanyName(), item.getName(), item.getMaxBalance()));
			item.getProductOptions().forEach(option -> {
				log.debug(String.format("\t금리유형: %s, 이자율: %s", option.getInterestRateType(), option.getInterestRates().toString()));
			});
		});
		log.debug("");
		
		ProductFilter productsFilter = new ProductFilter(balance);
		List<Product> filtered = productsFilter.filter(products);
		assertThat(filtered.size()).isEqualTo(4);
		assertThat(filtered.get(0)).matches(p -> p.getCode().equals("HK00001") && p.getCompanyNumber().equals("0010345"));
		assertThat(filtered.get(1)).matches(p -> p.getCode().equals("24000") && p.getCompanyNumber().equals("0010346"));
		assertThat(filtered.get(2)).matches(p -> p.getCode().equals("2407") && p.getCompanyNumber().equals("0010349"));
		assertThat(filtered.get(3)).matches(p -> p.getCode().equals("2400") && p.getCompanyNumber().equals("0010349"));
		
		filtered.stream().forEach(item -> {
			log.debug(String.format("은행코드: %s, 상품코드: %s, 은행명: %s, 상품명: %s, 가입한도: %,d원", item.getCompanyNumber(), item.getCode(), item.getCompanyName(), item.getName(), item.getMaxBalance()));
			item.getProductOptions().stream()
				.filter(option -> option.getId() == item.getFilteredOptionId())
				.findAny()
				.ifPresent(option -> {
					log.debug(String.format("\t금리유형: %s", option.getInterestRateType()));
					option.getInterestRates().stream()
						.forEach(ir -> {
							log.debug(String.format("\t\t기간: %d개월, 금리: %.2f, 우대금리: %.2f", ir.getContractPeriod(), ir.getRate(), ir.getPrimeRate()));
						});
				});
			log.debug(String.format("원금합계: %,d원, 세전이자: %,d원, 이자과세: %,d원, 세후 총 수령액: %,d원", item.getTotalPrincipal(), item.getInterestBeforeTax(), item.getInterestIncomeTax(), item.getAfterPayingTax()));
		});
	}

	private List<Product> createDummyDepositProducts() {
		List<Product> products = new ArrayList<>();
		Product product = new Product();
		product.setProductType(ProductType.FIXED_DEPOSIT);
		product.setCompanyNumber("0010345");
		product.setCompanyName("애큐온저축은행");
		product.setCode("HK00001");
		product.setName("정기예금");
		product.setMinPeriod(6);
		product.setMaxPeriod(36);
		product.setPeriodUnit(ChronoUnit.MONTHS);
		product.setMinBalance(0);
		product.setMaxBalance(0);
		
		ProductOption productOption = new ProductOption();
		productOption.setId(210l);
		productOption.setInterestRateType(InterestRateType.SIMPLE);
		productOption.setReserveType(ReserveType.FIXED);
		productOption.getInterestRates().addAll(
				Arrays.asList(
						new InterestRate(6, 0.018, 0.019), 
						new InterestRate(12, 0.025, 0.026),
						new InterestRate(24, 0.028, 0.029),
						new InterestRate(36, 0.029, 0.03)));
		product.getProductOptions().add(productOption);
		
		productOption = new ProductOption();
		productOption.setId(211l);
		productOption.setInterestRateType(InterestRateType.COMPOUND);
		productOption.setReserveType(ReserveType.FIXED);
		productOption.getInterestRates().addAll(
				Arrays.asList(
						new InterestRate(6, 0.018, 0.019), 
						new InterestRate(12, 0.025, 0.026),
						new InterestRate(24, 0.028, 0.029),
						new InterestRate(36, 0.029, 0.03)));
		product.getProductOptions().add(productOption);
		products.add(product);
		
		product = new Product();
		product.setProductType(ProductType.FIXED_DEPOSIT);
		product.setCompanyNumber("0010346");
		product.setCompanyName("OSB저축은행");
		product.setCode("24000");
		product.setName("정기예금");
		product.setMinPeriod(6);
		product.setMaxPeriod(36);
		product.setPeriodUnit(ChronoUnit.MONTHS);
		product.setMinBalance(0);
		product.setMaxBalance(0);
		
		productOption = new ProductOption();
		productOption.setId(213l);
		productOption.setInterestRateType(InterestRateType.SIMPLE);
		productOption.setReserveType(ReserveType.FIXED);
		productOption.getInterestRates().addAll(
				Arrays.asList(
						new InterestRate(6, 0.016, 0.016), 
						new InterestRate(12, 0.025, 0.026),
						new InterestRate(24, 0.027, 0.027),
						new InterestRate(36, 0.028, 0.028)));
		product.getProductOptions().add(productOption);
		
		productOption = new ProductOption();
		productOption.setId(214l);
		productOption.setInterestRateType(InterestRateType.COMPOUND);
		productOption.setReserveType(ReserveType.FIXED);
		productOption.getInterestRates().addAll(
				Arrays.asList(
						new InterestRate(6, 0.016, 0.016), 
						new InterestRate(12, 0.025, 0.026),
						new InterestRate(24, 0.027, 0.027),
						new InterestRate(36, 0.028, 0.028)));
		product.getProductOptions().add(productOption);
		products.add(product);
		
		product = new Product();
		product.setProductType(ProductType.FIXED_DEPOSIT);
		product.setCompanyNumber("0010346");
		product.setCompanyName("OSB저축은행");
		product.setCode("24001");
		product.setName("CD연동장기정기예금");
		product.setMinPeriod(24);
		product.setMaxPeriod(36);
		product.setPeriodUnit(ChronoUnit.MONTHS);
		product.setMinBalance(0);
		product.setMaxBalance(0);
		
		productOption = new ProductOption();
		productOption.setId(216l);
		productOption.setInterestRateType(InterestRateType.SIMPLE);
		productOption.setReserveType(ReserveType.FIXED);
		productOption.getInterestRates().addAll(
				Arrays.asList(
						new InterestRate(24, 0.028, 0.028),
						new InterestRate(36, 0.029, 0.029)));
		product.getProductOptions().add(productOption);
		products.add(product);
		
		product = new Product();
		product.setProductType(ProductType.FIXED_DEPOSIT);
		product.setCompanyNumber("0010349");
		product.setCompanyName("디비저축은행");
		product.setCode("2400");
		product.setName("정기예금");
		product.setMinPeriod(6);
		product.setMaxPeriod(24);
		product.setPeriodUnit(ChronoUnit.MONTHS);
		product.setMinBalance(0);
		product.setMaxBalance(0);
		
		productOption = new ProductOption();
		productOption.setId(218l);
		productOption.setInterestRateType(InterestRateType.SIMPLE);
		productOption.setReserveType(ReserveType.FIXED);
		productOption.getInterestRates().addAll(
				Arrays.asList(
						new InterestRate(6, 0.015, 0.016),
						new InterestRate(12, 0.023, 0.024),
						new InterestRate(24, 0.023, 0.024)));
		product.getProductOptions().add(productOption);
		
		productOption = new ProductOption();
		productOption.setId(219l);
		productOption.setInterestRateType(InterestRateType.COMPOUND);
		productOption.setReserveType(ReserveType.FIXED);
		productOption.getInterestRates().addAll(
				Arrays.asList(
						new InterestRate(6, 0.015, 0.016),
						new InterestRate(12, 0.023, 0.024),
						new InterestRate(24, 0.023, 0.024)));
		product.getProductOptions().add(productOption);
		products.add(product);
		
		product = new Product();
		product.setProductType(ProductType.FIXED_DEPOSIT);
		product.setCompanyNumber("0010349");
		product.setCompanyName("디비저축은행");
		product.setCode("2407");
		product.setName("E-정기예금");
		product.setMinPeriod(6);
		product.setMaxPeriod(24);
		product.setPeriodUnit(ChronoUnit.MONTHS);
		product.setMinBalance(0);
		product.setMaxBalance(0);
		
		productOption = new ProductOption();
		productOption.setId(221l);
		productOption.setInterestRateType(InterestRateType.SIMPLE);
		productOption.setReserveType(ReserveType.FIXED);
		productOption.getInterestRates().addAll(
				Arrays.asList(
						new InterestRate(6, 0.016, 0.016),
						new InterestRate(12, 0.024, 0.024),
						new InterestRate(24, 0.024, 0.024)));
		product.getProductOptions().add(productOption);
		
		productOption = new ProductOption();
		productOption.setId(222l);
		productOption.setInterestRateType(InterestRateType.COMPOUND);
		productOption.setReserveType(ReserveType.FIXED);
		productOption.getInterestRates().addAll(
				Arrays.asList(
						new InterestRate(6, 0.016, 0.016),
						new InterestRate(12, 0.024, 0.024),
						new InterestRate(24, 0.024, 0.024)));
		product.getProductOptions().add(productOption);
		products.add(product);
		return products;
	}
	
	@Test
	public void givenSavingProductsThenReturnFilteredProductList() {
		List<Product> products = createDummySavingProducts();
		products.stream().forEach(item -> {
			log.debug(String.format("은행코드: %s, 상품코드: %s, 은행명: %s, 상품명: %s, 가입한도: %,d원", item.getCompanyNumber(), item.getCode(), item.getCompanyName(), item.getName(), item.getMaxBalance()));
			item.getProductOptions().forEach(option -> {
				log.debug(String.format("\t금리유형: %s, 이자율: %s", option.getInterestRateType(), option.getInterestRates().toString()));
			});
		});
		log.debug("");
		
		assertThat(products.size()).isEqualTo(5);
		assertThat(products.get(0)).matches(p -> p.getCompanyNumber().equals("0010002") && p.getCode().equals("00266451"));
		assertThat(products.get(1)).matches(p -> p.getCompanyNumber().equals("0010002") && p.getCode().equals("00266472"));
		assertThat(products.get(2)).matches(p -> p.getCompanyNumber().equals("0010006") && p.getCode().equals("1800"));
		assertThat(products.get(3)).matches(p -> p.getCompanyNumber().equals("0010006") && p.getCode().equals("1813"));
		assertThat(products.get(4)).matches(p -> p.getCompanyNumber().equals("0010016") && p.getCode().equals("10521001000846001"));
		
		long balance = 1800000;
		
		ProductFilter productsFilter = new ProductFilter(balance);
		List<Product> filtered = productsFilter.filter(products);
		filtered.stream().forEach(item -> {
			log.debug(String.format("은행코드: %s, 상품코드: %s, 은행명: %s, 상품명: %s, 가입한도: %,d원", item.getCompanyNumber(), item.getCode(), item.getCompanyName(), item.getName(), item.getMaxBalance()));
			item.getProductOptions().stream()
				.filter(option -> option.getId() == item.getFilteredOptionId())
				.findAny()
				.ifPresent(option -> {
					log.debug(String.format("\t금리유형: %s, 적립유형: %s", option.getInterestRateType(), option.getReserveType()));
					option.getInterestRates().stream()
					.forEach(ir -> {
						log.debug(String.format("\t\t기간: %d개월, 금리: %.2f, 우대금리: %.2f", ir.getContractPeriod(), ir.getRate(), ir.getPrimeRate()));
					});
				});
			log.debug(String.format("원금합계: %,d원, 세전이자: %,d원, 이자과세: %,d원, 세후 총 수령액: %,d원", item.getTotalPrincipal(), item.getInterestBeforeTax(), item.getInterestIncomeTax(), item.getAfterPayingTax()));
		});
		
		assertThat(filtered.size()).isEqualTo(3);
		assertThat(filtered.get(0)).matches(p -> p.getCompanyNumber().equals("0010002") && p.getCode().equals("00266451"));
		assertThat(filtered.get(1)).matches(p -> p.getCompanyNumber().equals("0010006") && p.getCode().equals("1800"));
		assertThat(filtered.get(2)).matches(p -> p.getCompanyNumber().equals("0010006") && p.getCode().equals("1813"));
	}
	
	private List<Product> createDummySavingProducts() {
		ProductType productType = ProductType.INSTALLMENT_SAVING;
		List<Product> products = new ArrayList<>();
		Product product = new Product();
		product.setProductType(productType);
		product.setCompanyNumber("0010002");
		product.setCompanyName("한국스탠다드차타드은행");
		product.setCode("00266451");
		product.setName("퍼스트가계적금");
		product.setMinPeriod(6);
		product.setMaxPeriod(36);
		product.setPeriodUnit(ChronoUnit.MONTHS);
		product.setMinBalance(0);
		product.setMaxBalance(10000000);
		
		ProductOption productOption = new ProductOption();
		productOption.setId(204l);
		productOption.setInterestRateType(InterestRateType.SIMPLE);
		productOption.setReserveType(ReserveType.FREE);
		productOption.getInterestRates().addAll(
				Arrays.asList(
						new InterestRate(6, 0.015, 0.015), 
						new InterestRate(12, 0.017, 0.017),
						new InterestRate(24, 0.018, 0.018),
						new InterestRate(36, 0.019, 0.019)));
		product.getProductOptions().add(productOption);
		
		productOption = new ProductOption();
		productOption.setId(205l);
		productOption.setInterestRateType(InterestRateType.SIMPLE);
		productOption.setReserveType(ReserveType.FIXED);
		productOption.getInterestRates().addAll(
				Arrays.asList(
						new InterestRate(6, 0.016, 0.016), 
						new InterestRate(12, 0.019, 0.019),
						new InterestRate(24, 0.02, 0.02),
						new InterestRate(36, 0.021, 0.021)));
		product.getProductOptions().add(productOption);
		products.add(product);
		
		product = new Product();
		product.setProductType(productType);
		product.setCompanyNumber("0010002");
		product.setCompanyName("한국스탠다드차타드은행");
		product.setCode("00266472");
		product.setName("SC행복적금");
		product.setMinPeriod(12);
		product.setMaxPeriod(12);
		product.setPeriodUnit(ChronoUnit.MONTHS);
		product.setMinBalance(0);
		product.setMaxBalance(300000);
		
		productOption = new ProductOption();
		productOption.setId(207l);
		productOption.setInterestRateType(InterestRateType.SIMPLE);
		productOption.setReserveType(ReserveType.FIXED);
		productOption.getInterestRates().addAll(
				Arrays.asList(
						new InterestRate(12, 0.047, 0.05)));
		product.getProductOptions().add(productOption);
		products.add(product);
		
		product = new Product();
		product.setProductType(productType);
		product.setCompanyNumber("0010006");
		product.setCompanyName("한국씨티은행");
		product.setCode("1800");
		product.setName("정기적금");
		product.setMinPeriod(12);
		product.setMaxPeriod(36);
		product.setPeriodUnit(ChronoUnit.MONTHS);
		product.setMinBalance(0);
		product.setMaxBalance(0);
		
		productOption = new ProductOption();
		productOption.setId(209l);
		productOption.setInterestRateType(InterestRateType.SIMPLE);
		productOption.setReserveType(ReserveType.FREE);
		productOption.getInterestRates().addAll(
				Arrays.asList(
						new InterestRate(12, 0.015, 0.015),
						new InterestRate(24, 0.016, 0.016),
						new InterestRate(36, 0.017, 0.017)));
		product.getProductOptions().add(productOption);
		products.add(product);
		
		product = new Product();
		product.setProductType(productType);
		product.setCompanyNumber("0010006");
		product.setCompanyName("한국씨티은행");
		product.setCode("1813");
		product.setName("원더풀라이프적금");
		product.setMinPeriod(12);
		product.setMaxPeriod(36);
		product.setPeriodUnit(ChronoUnit.MONTHS);
		product.setMinBalance(0);
		product.setMaxBalance(10000000);
		
		productOption = new ProductOption();
		productOption.setId(211l);
		productOption.setInterestRateType(InterestRateType.SIMPLE);
		productOption.setReserveType(ReserveType.FREE);
		productOption.getInterestRates().addAll(
				Arrays.asList(
						new InterestRate(12, 0.015, 0.02),
						new InterestRate(24, 0.016, 0.021),
						new InterestRate(36, 0.017, 0.022)));
		product.getProductOptions().add(productOption);
		products.add(product);
		
		product = new Product();
		product.setProductType(productType);
		product.setCompanyNumber("0010016");
		product.setCompanyName("대구은행");
		product.setCode("10521001000846001");
		product.setName("내손안에 적금");
		product.setMinPeriod(12);
		product.setMaxPeriod(36);
		product.setPeriodUnit(ChronoUnit.MONTHS);
		product.setMinBalance(0);
		product.setMaxBalance(1000000);
		
		productOption = new ProductOption();
		productOption.setId(213l);
		productOption.setInterestRateType(InterestRateType.SIMPLE);
		productOption.setReserveType(ReserveType.FIXED);
		productOption.getInterestRates().addAll(
				Arrays.asList(
						new InterestRate(12, 0.0196, 0.0251),
						new InterestRate(24, 0.0198, 0.0253),
						new InterestRate(36, 0.02, 0.0255)));
		product.getProductOptions().add(productOption);
		products.add(product);
		return products;
	}
	
}
