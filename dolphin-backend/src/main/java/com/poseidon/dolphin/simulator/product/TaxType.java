package com.poseidon.dolphin.simulator.product;

/**
 * @author gang-yeongho
 * 이자과세유형
 */
public enum TaxType {
	/**
	 * 일반
	 */
	GENERAL(0.154), 
	/**
	 * 비과세
	 */
	NON_TAXABLE(0), 
	/**
	 * 세금우대
	 */
	TAX_BREAKS(0);
	
	private final double taxRate;
	TaxType(final double taxRate) {
		this.taxRate = taxRate;
	}
	
	public double getTaxRate() {
		return taxRate;
	}
}
