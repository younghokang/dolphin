package com.poseidon.dolphin.api.fss.result;

import java.util.Arrays;

public enum ErrorCode {
	SUCCESS("000", "정상"),
	NON_AUTH_KEY("010", "미등록 인증키"),
	EXPIRED_AUTH_KEY("011", "중지된 인증키"),
	DELETED_AUTH_KEY("012", "삭제된 인증키"),
	SAMPLE_AUTH_KEY("013", "샘플 인증키"),
	SEARCH_LIMIT("020", "일일검색 허용횟수 초과"),
	NOT_ALLOWED_IP("021", "허용된 IP가 아닙니다."),
	MISSING_ARGS("100", "{요청변수ID}누락"),
	ILLEGAL_ARGS("101", "{요청변수ID}의 부적절한 값"),
	UNKNOWN("900", "정의되지 않은 오류");
	
	private final String code;
	private final String description;
	ErrorCode(final String code, final String description) {
		this.code = code;
		this.description = description;
	}
	public String getCode() {
		return code;
	}
	public String getDescription() {
		return description;
	}
	
	public static ErrorCode findByCode(final String code) {
		return Arrays.stream(values())
				.filter(obj -> obj.getCode().equalsIgnoreCase(code))
				.findAny()
				.orElseThrow(UnsupportedOperationException::new);
	}
}
