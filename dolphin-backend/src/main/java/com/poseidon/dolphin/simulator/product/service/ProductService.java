package com.poseidon.dolphin.simulator.product.service;

import java.util.List;

import com.poseidon.dolphin.api.fss.deposit.Deposit;
import com.poseidon.dolphin.api.fss.saving.Saving;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductCommand;

public interface ProductService {
	Product save(ProductCommand productCommand);
	List<Product> mergeToSavings(List<Saving> savings);
	List<Product> mergeToDeposits(List<Deposit> deposits);
}
