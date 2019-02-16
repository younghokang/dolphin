package com.poseidon.dolphin.simulator.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.dolphin.api.fss.saving.Saving;
import com.poseidon.dolphin.simulator.company.repository.FinanceCompanyRepository;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductType;
import com.poseidon.dolphin.simulator.product.repository.ProductRepository;

@RunWith(SpringRunner.class)
public class ProductServiceTests {
	private ProductService productService;
	
	@MockBean
	private ProductRepository productRepository;
	
	@MockBean
	private FinanceCompanyRepository financeCompanyRepository;
	
	@Before
	public void setUp() {
		productService = new ProductServiceImpl(productRepository, financeCompanyRepository);
	}
	
	@Test
	public void givenCollectionsThenMerge() {
		given(productRepository.findByCodeAndCompanyNumberAndProductType(anyString(), anyString(), any(ProductType.class))).willReturn(Optional.empty());
		given(financeCompanyRepository.findByCode(anyString())).willReturn(Optional.empty());
		
		Product product = new Product();
		given(productRepository.save(any(Product.class))).willReturn(product);
		
		List<Saving> savings = new ArrayList<>();
		Saving saving = new Saving();
		saving.setFinanceCompanyNumber("AAAA");
		saving.setFinanceProductCode("BBBBB");
		savings.add(saving);
		assertThat(productService.mergeToSavings(savings)).isNotEmpty();
		
		verify(productRepository, times(1)).findByCodeAndCompanyNumberAndProductType(anyString(), anyString(), any(ProductType.class));
		verify(financeCompanyRepository, times(1)).findByCode(anyString());
		verify(productRepository, times(1)).save(any(Product.class));
	}

}
