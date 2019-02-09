package com.poseidon.dolphin.simulator.product;

import java.util.Arrays;

/**
 * @author gang-yeongho
 * 상품유형
 */
public enum ProductType {
	/**
	 * 적금
	 */
	INSTALLMENT_SAVING("SAVING"),
	/**
	 * 정기예금
	 */
	FIXED_DEPOSIT("DEPOSIT");
	
	private final String code;
	ProductType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static ProductType findByCode(String code) {
		return Arrays.stream(values())
				.filter(obj -> obj.getCode().equals(code))
				.findAny()
				.orElseThrow(UnsupportedOperationException::new);
	}

}
