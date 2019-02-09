package com.poseidon.dolphin.simulator.product;

import javax.persistence.Embeddable;

import com.poseidon.dolphin.api.fss.deposit.DepositOption;
import com.poseidon.dolphin.api.fss.saving.SavingOption;

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

	public static InterestRate from(SavingOption savingOption) {
		return new InterestRate(savingOption.getSaveTerm(), savingOption.getInterestRate() * 0.01, savingOption.getPrimeRate() * 0.01);
	}
	
	public static InterestRate from(DepositOption depositOption) {
		return new InterestRate(depositOption.getSaveTerm(), depositOption.getInterestRate() * 0.01, depositOption.getPrimeRate() * 0.01);
	}

	@Override
	public String toString() {
		return "InterestRate [contractPeriod=" + contractPeriod + ", rate=" + rate + ", primeRate=" + primeRate + "]";
	}

}
