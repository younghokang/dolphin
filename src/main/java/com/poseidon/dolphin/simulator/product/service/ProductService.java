package com.poseidon.dolphin.simulator.product.service;

import java.util.List;

import com.poseidon.dolphin.simulator.TestState;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductType;

public interface ProductService {
	List<Product> findAllByProductTypeAndTestState(ProductType productType, TestState testState);
	List<Product> cachePutFindAllByProductTypeAndTestState(ProductType productType, TestState testState);
	Product findById(long id);
}
