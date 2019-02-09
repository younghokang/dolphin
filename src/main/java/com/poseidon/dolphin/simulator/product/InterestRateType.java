package com.poseidon.dolphin.simulator.product;

import java.util.Arrays;

/**
 * @author gang-yeongho
 * 저축 금리 유형: 단리/복리
 */
public enum InterestRateType {
	SIMPLE("S"), COMPOUND("M");
	
	private final String code;
	InterestRateType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static InterestRateType findByCode(String code) {
		return Arrays.stream(values())
				.filter(obj -> obj.getCode().equals(code))
				.findAny()
				.orElseThrow(UnsupportedOperationException::new);
	}
}
