package com.poseidon.dolphin.simulator.account;

import java.time.LocalDate;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.poseidon.dolphin.api.fss.common.ReserveType;
import com.poseidon.dolphin.api.fss.common.converter.ProductTypeConverter;
import com.poseidon.dolphin.api.fss.common.converter.ReserveTypeConverter;
import com.poseidon.dolphin.simulator.product.Interest;
import com.poseidon.dolphin.simulator.product.ProductType;
import com.poseidon.dolphin.simulator.product.TaxType;

@Embeddable
public class Contract {
	private String name;
	@Convert(converter=ProductTypeConverter.class)
	private ProductType productType;
	private long balance;
	@Enumerated(EnumType.STRING)
	private Interest interest;
	private double interestRate;
	private LocalDate contractDate;
	private LocalDate expiryDate;
	@Enumerated(EnumType.STRING)
	private PaymentFrequency paymentFrequency;
	private double taxRate;
	@Enumerated(EnumType.STRING)
	private TaxType taxType;
	@Convert(converter=ReserveTypeConverter.class)
	private ReserveType reserveType;
	private int dateOfPayment;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public Interest getInterest() {
		return interest;
	}
	public void setInterest(Interest interest) {
		this.interest = interest;
	}
	public double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	public LocalDate getContractDate() {
		return contractDate;
	}
	public void setContractDate(LocalDate contractDate) {
		this.contractDate = contractDate;
	}
	public LocalDate getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}
	public PaymentFrequency getPaymentFrequency() {
		return paymentFrequency;
	}
	public void setPaymentFrequency(PaymentFrequency paymentFrequency) {
		this.paymentFrequency = paymentFrequency;
	}
	public double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}
	public TaxType getTaxType() {
		return taxType;
	}
	public void setTaxType(TaxType taxType) {
		this.taxType = taxType;
	}
	public ReserveType getReserveType() {
		return reserveType;
	}
	public void setReserveType(ReserveType reserveType) {
		this.reserveType = reserveType;
	}
	public int getDateOfPayment() {
		return dateOfPayment;
	}
	public void setDateOfPayment(int dateOfPayment) {
		this.dateOfPayment = dateOfPayment;
	}

	@Override
	public String toString() {
		return "Contract [name=" + name + ", productType=" + productType + ", balance=" + balance + ", interest="
				+ interest + ", interestRate=" + interestRate + ", contractDate=" + contractDate + ", expiryDate="
				+ expiryDate + ", paymentFrequency=" + paymentFrequency + ", taxRate=" + taxRate + ", taxType="
				+ taxType + ", reserveType=" + reserveType + ", dateOfPayment=" + dateOfPayment + "]";
	}
	
}
