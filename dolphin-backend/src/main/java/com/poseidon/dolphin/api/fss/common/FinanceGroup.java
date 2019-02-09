package com.poseidon.dolphin.api.fss.common;

import java.util.Arrays;

public enum FinanceGroup {
	BANK("020000", "은행", 0),
	LOAN("030200", "여신전문", 1),
	SAVING_BANK("030300", "저축은행", 2),
	INSURANCE("050000", "보험", 3),
	INVESTMENT("060000", "금융투자", 4);
	
	private final String code;
	private final String name;
	private final int numberOfOrder;
	FinanceGroup(final String code, final String name, final int numberOfOrder) {
		this.code = code;
		this.name = name;
		this.numberOfOrder = numberOfOrder;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public int getNumberOfOrder() {
		return numberOfOrder;
	}
	
	public static FinanceGroup findByCode(final String code) {
		return Arrays.stream(values()).filter(obj -> obj.getCode().equals(code))
			.findAny()
			.orElseThrow(UnsupportedOperationException::new);
	}
}
