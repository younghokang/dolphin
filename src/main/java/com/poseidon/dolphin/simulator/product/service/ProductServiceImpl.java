package com.poseidon.dolphin.simulator.product.service;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.poseidon.dolphin.simulator.TestState;
import com.poseidon.dolphin.simulator.filter.ProductFilter;
import com.poseidon.dolphin.simulator.product.JoinDeny;
import com.poseidon.dolphin.simulator.product.NotFoundProductException;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductType;
import com.poseidon.dolphin.simulator.product.repository.ProductRepository;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
	private static final long MAX_SIZE = 3;
	private final ProductRepository productRepository;
	
	@Autowired
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	@Override
	public List<Product> getFilteredProductList(ProductType productType, long balance, Set<String> excludeCompanyNumbers) {
		return this.getFilteredProductList(productType, balance, 0, null, excludeCompanyNumbers);
	}
	
	@Override
	public List<Product> getFilteredProductList(ProductType productType, long balance, int period, ChronoUnit periodUnit, Set<String> excludeCompanyNumbers) {
		return this.getFilteredProductList(productType, balance, period, periodUnit, JoinDeny.NONE, excludeCompanyNumbers);
	}
	
	@Override
	public List<Product> getFilteredProductList(ProductType productType, long balance, int period, ChronoUnit periodUnit, JoinDeny joinDeny, Set<String> excludeCompanyNumbers) {
		Assert.notNull(joinDeny, "joinDeny must not be null");
		Assert.notNull(productType, "productType must not be null");
		Assert.isTrue(balance > Product.DEFAULT_MIN_BALANCE, "balance must be greater than " + Product.DEFAULT_MIN_BALANCE);
		
		List<Product> products = productRepository.findAllByProductTypeAndTestState(productType, TestState.DONE).stream()
				.filter(p -> p.getJoinDeny().equals(joinDeny) && !excludeCompanyNumbers.contains(p.getCompanyNumber()))
				.collect(Collectors.toList());
		if(products == null) {
			return Collections.emptyList();
		}
		ProductFilter productFilter = new ProductFilter(balance);
		if(period > 0) {
			productFilter.setPeriod(period);
		}
		if(periodUnit != null) {
			productFilter.setPeriodUnit(periodUnit);
		}
		return productFilter.filter(products)
				.stream()
				.limit(MAX_SIZE)
				.collect(Collectors.toList());
	}
	
	@Override
	public Product getFilteredProductById(long id, long balance) {
		return this.getFilteredProductById(id, balance, null, null);
	}
	
	@Override
	public Product getFilteredProductById(long id, long balance, Integer period, ChronoUnit periodUnit) {
		Assert.isTrue(id > 0, "productId must be greater than zero");
		Assert.isTrue(balance > Product.DEFAULT_MIN_BALANCE, "balance must be greater than " + Product.DEFAULT_MIN_BALANCE);
		
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new NotFoundProductException(id));
		ProductFilter productFilter = new ProductFilter(balance);
		if(period != null) {
			productFilter.setPeriod(period);
		}
		if(periodUnit != null) {
			productFilter.setPeriodUnit(periodUnit);
		}
		productFilter.filter(product);
		return product;
	}
	
}
