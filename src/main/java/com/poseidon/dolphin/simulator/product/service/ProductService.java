package com.poseidon.dolphin.simulator.product.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import com.poseidon.dolphin.simulator.product.JoinDeny;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductType;

public interface ProductService {
	List<Product> getFilteredProductList(ProductType productType, long balance, Set<String> excludeCompanyNumbers);
	List<Product> getFilteredProductList(ProductType productType, long balance, int period, ChronoUnit periodUnit, Set<String> excludeCompanyNumbers);
	List<Product> getFilteredProductList(ProductType productType, long balance, int period, ChronoUnit periodUnit, JoinDeny joinDeny, Set<String> excludeCompanyNumbers);
	Product getFilteredProductById(long id, long balance);
	Product getFilteredProductById(long id, long balance, Integer period, ChronoUnit periodUnit);
}
