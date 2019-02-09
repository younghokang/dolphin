package com.poseidon.dolphin.simulator.product;

import java.util.Arrays;

/**
 * @author gang-yeongho
 * 적립 유형: 자유적립식/정액적립식
 */
public enum ReserveType {
	FREE("F"), FIXED("S");
	
	private final String code;
	ReserveType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static ReserveType findByCode(String code) {
		return Arrays.stream(values())
			.filter(obj -> obj.getCode().equals(code))
			.findAny()
			.orElseThrow(UnsupportedOperationException::new);
	}
	
}
