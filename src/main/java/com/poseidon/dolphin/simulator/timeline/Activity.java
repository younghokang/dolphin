package com.poseidon.dolphin.simulator.timeline;

import java.util.Arrays;

public enum Activity {
	INSTALLMENT_SAVING_ACCOUNT_OPEN("ISAO", "적금계좌 개설"),
	INSTALLMENT_SAVING_REGULARY_PAYMENT("ISRP", "적금 납입"),
	INSTALLMENT_SAVING_ACCOUNT_CLOSE("ISAC", "적금계좌 해지"),
	FIXED_DEPOSIT_ACCOUNT_OPEN("FDAO", "예금계좌 개설"),
	FIXED_DEPOSIT_ACCOUNT_CLOSE("FDAC", "예금계좌 해지");
	
	private final String code;
	private final String description;
	Activity(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String getCode() {
		return code;
	}
	public String getDescription() {
		return description;
	}
	
	public static Activity findByCode(String code) {
		return Arrays.stream(values())
			.filter(p -> p.getCode().equals(code))
			.findAny()
			.orElseThrow(UnsupportedOperationException::new);
	}
	
}
