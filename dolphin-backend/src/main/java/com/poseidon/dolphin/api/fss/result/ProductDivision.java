package com.poseidon.dolphin.api.fss.result;

import java.util.Arrays;

public enum ProductDivision {
	COMPANY("F"),
	SAVING("S"),
	DEPOSIT("D");
	
	private final String code;
	ProductDivision(final String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	
	public static ProductDivision findByCode(final String code) {
		return Arrays.stream(values()).filter(obj -> obj.getCode().equalsIgnoreCase(code))
			.findAny().orElseThrow(UnsupportedOperationException::new);
	}
}
