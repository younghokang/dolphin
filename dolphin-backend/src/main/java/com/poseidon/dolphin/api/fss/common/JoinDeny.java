package com.poseidon.dolphin.api.fss.common;

import java.util.Arrays;

public enum JoinDeny {
	NONE(1, "제한없음"),
	ORDINARY(2, "서민전용"),
	PARTIAL(3, "일부제한");
	
	private final int code;
	private final String value;
	JoinDeny(int code, String value) {
		this.code = code;
		this.value = value;
	}
	
	public int getCode() {
		return code;
	}
	public String getValue() {
		return value;
	}
	
	public static JoinDeny findByCode(final int code) {
		return Arrays.stream(values())
				.filter(obj -> obj.getCode() == code)
				.findAny()
				.orElseThrow(UnsupportedOperationException::new);
	}
}
