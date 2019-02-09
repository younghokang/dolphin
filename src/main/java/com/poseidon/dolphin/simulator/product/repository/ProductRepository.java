package com.poseidon.dolphin.simulator.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.dolphin.simulator.TestState;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductType;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findAllByProductTypeAndTestState(ProductType productType, TestState testState);
	Optional<Product> findByCode(String code);
	long countByProductType(ProductType productType);
}
