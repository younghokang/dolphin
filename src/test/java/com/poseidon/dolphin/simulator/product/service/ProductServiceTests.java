package com.poseidon.dolphin.simulator.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.dolphin.simulator.TestState;
import com.poseidon.dolphin.simulator.product.InterestRate;
import com.poseidon.dolphin.simulator.product.InterestRateType;
import com.poseidon.dolphin.simulator.product.JoinDeny;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductOption;
import com.poseidon.dolphin.simulator.product.ProductType;
import com.poseidon.dolphin.simulator.product.ReserveType;
import com.poseidon.dolphin.simulator.product.repository.ProductRepository;

@RunWith(SpringRunner.class)
public class ProductServiceTests {
	private ProductService productService;
	
	@MockBean
	private ProductRepository productRepository;
	
	@Before
	public void setUp() {
		productService = new ProductService(productRepository);
	}

	@Test
	public void whenCallGetFilteredProductListThenReturnFilteredProductList() {
		ProductType productType = ProductType.INSTALLMENT_SAVING;
		long balance = 1800000;
		
		given(productRepository.findAllByProductTypeAndTestState(any(ProductType.class), any(TestState.class))).willReturn(Collections.emptyList());
		
		List<Product> filteredProducts = productService.getFilteredProductList(productType, balance, null);
		assertThat(filteredProducts).isEmpty();
		
		Product product = new Product();
		product.setProductType(productType);
		product.setMinBalance(0);
		product.setMaxBalance(3000000);
		product.setMinPeriod(6);
		product.setMaxPeriod(36);
		product.setPeriodUnit(ChronoUnit.MONTHS);
		product.setJoinDeny(JoinDeny.NONE);
		
		ProductOption productOption = new ProductOption();
		productOption.setId(1L);
		productOption.setInterestRateType(InterestRateType.SIMPLE);
		productOption.setReserveType(ReserveType.FIXED);
		productOption.setInterestRates(
				Arrays.asList(
						new InterestRate(6, 0.015, 0.017), 
						new InterestRate(12, 0.025, 0.027), 
						new InterestRate(24, 0.028, 0.030)));
		product.getProductOptions().add(productOption);
		
		List<Product> products = Arrays.asList(product);
		given(productRepository.findAllByProductTypeAndTestState(any(ProductType.class), any(TestState.class))).willReturn(products);
		
		filteredProducts = productService.getFilteredProductList(productType, balance, Collections.emptySet());
		assertThat(filteredProducts).isNotNull();
		assertThat(filteredProducts.size()).isEqualTo(1);
		assertThat(filteredProducts.get(0).getFilteredOptionId()).isEqualTo(1L);
		
		verify(productRepository, times(2)).findAllByProductTypeAndTestState(productType, TestState.DONE);
	}
	
	@Test
	public void whenCallGetFilteredProductByIdThenReturnFilteredProduct() {
		long id = 1L;
		ProductType productType = ProductType.INSTALLMENT_SAVING;
		long balance = 1800000;
		int period = 12;
		ChronoUnit periodUnit = ChronoUnit.MONTHS;
		
		given(productRepository.findById(eq(2L))).willThrow(NullPointerException.class);
		
		try {
			productService.getFilteredProductById(2L, balance, period, periodUnit);
			fail();
		} catch(NullPointerException e) {}
		
		Product product = new Product();
		product.setProductType(productType);
		product.setMinBalance(0);
		product.setMaxBalance(3000000);
		product.setMinPeriod(6);
		product.setMaxPeriod(36);
		product.setPeriodUnit(periodUnit);
		
		ProductOption productOption = new ProductOption();
		productOption.setId(1L);
		productOption.setInterestRateType(InterestRateType.SIMPLE);
		productOption.setReserveType(ReserveType.FIXED);
		productOption.setInterestRates(
				Arrays.asList(
						new InterestRate(6, 0.015, 0.017), 
						new InterestRate(12, 0.025, 0.027), 
						new InterestRate(24, 0.028, 0.030)));
		product.getProductOptions().add(productOption);
		
		given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
		
		Product filteredProduct = productService.getFilteredProductById(id, balance, period, periodUnit);
		assertThat(filteredProduct).isEqualTo(product);
		
		verify(productRepository, times(2)).findById(anyLong());
	}
	
}
