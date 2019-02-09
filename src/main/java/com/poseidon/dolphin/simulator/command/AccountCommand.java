package com.poseidon.dolphin.simulator.command;

import javax.validation.constraints.Min;

import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductType;

public class AccountCommand {
	@Min(Product.DEFAULT_MIN_BALANCE)
	private long balance;
	private long productId;
	private ProductType productType;
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
}
