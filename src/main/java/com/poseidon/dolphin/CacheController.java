package com.poseidon.dolphin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poseidon.dolphin.simulator.TestState;
import com.poseidon.dolphin.simulator.product.ProductType;
import com.poseidon.dolphin.simulator.product.service.ProductService;

@RestController
@RequestMapping("/cache")
public class CacheController {
	private final ProductService productService;
	
	@Autowired
	public CacheController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping("/products/{productType}")
	public ResponseEntity<Void> productsCaching(@PathVariable("productType") ProductType productType) {
		productService.cachePutFindAllByProductTypeAndTestState(productType, TestState.DONE);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
