package com.poseidon.dolphin.simulator.product.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poseidon.dolphin.simulator.TestState;
import com.poseidon.dolphin.simulator.product.NotFoundProductException;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductType;
import com.poseidon.dolphin.simulator.product.repository.ProductRepository;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
	Logger log = LoggerFactory.getLogger(this.getClass());
	private final ProductRepository productRepository;
	
	@Autowired
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	@Cacheable("products")
	public List<Product> findAllByProductTypeAndTestState(ProductType productType, TestState testState) {
		return productRepository.findAllByProductTypeAndTestState(productType, testState);
	}

	@Override
	@CachePut("products")
	public List<Product> cachePutFindAllByProductTypeAndTestState(ProductType productType, TestState testState) {
		return productRepository.findAllByProductTypeAndTestState(productType, testState);
	}

	@Override
	public Product findById(long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new NotFoundProductException(id));
	}
	
}
