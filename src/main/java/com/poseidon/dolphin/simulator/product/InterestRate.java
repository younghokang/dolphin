package com.poseidon.dolphin.simulator.product;

import javax.persistence.Embeddable;

@Embeddable
public class InterestRate {
	private int contractPeriod;
	private double rate;
	private double primeRate;
	
	protected InterestRate() {}
	
	public InterestRate(int contractPeriod, double rate, double primeRate) {
		super();
		this.contractPeriod = contractPeriod;
		this.rate = rate;
		this.primeRate = primeRate;
	}
	
	public int getContractPeriod() {
		return contractPeriod;
	}
	public double getRate() {
		return rate;
	}
	public double getPrimeRate() {
		return primeRate;
	}
	public void setContractPeriod(int contractPeriod) {
		this.contractPeriod = contractPeriod;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public void setPrimeRate(double primeRate) {
		this.primeRate = primeRate;
	}

	@Override
	public String toString() {
		return "InterestRate [contractPeriod=" + contractPeriod + ", rate=" + rate + ", primeRate=" + primeRate + "]";
	}

}
