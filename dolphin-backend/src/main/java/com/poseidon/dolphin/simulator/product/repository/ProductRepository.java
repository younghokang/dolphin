package com.poseidon.dolphin.simulator.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductType;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> findByCodeAndCompanyNumberAndProductType(String code, String companyNumber, ProductType productType);
}
