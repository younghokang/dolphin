package com.poseidon.dolphin.simulator.wallet;

import java.util.Arrays;
import java.util.function.Function;

import com.poseidon.dolphin.simulator.timeline.Activity;

/**
 * @author gang-yeongho
 * 이체 유형
 */
public enum TransferType {
	/**
	 * 적금 해지
	 */
	INSTALLMENT_SAVING_CLOSE("ISC", Activity.INSTALLMENT_SAVING_ACCOUNT_CLOSE, balance -> Math.abs(balance)),
	/**
	 * 예금 해지
	 */
	FIXED_DEPOSIT_CLOSE("FDC", Activity.FIXED_DEPOSIT_ACCOUNT_CLOSE, balance -> Math.abs(balance)),
	/**
	 * 예금 개설
	 */
	FIXED_DEPOSIT_OPEN("FDO", Activity.FIXED_DEPOSIT_ACCOUNT_OPEN, balance -> -balance);
	
	private Function<Long, Long> expression;
	private final String code;
	private final Activity activity;
	TransferType(String code, Activity activity, Function<Long, Long> expression) {
		this.code = code;
		this.activity = activity;
		this.expression = expression;
	}
	
	public String getCode() {
		return code;
	}
	public Activity getActivity() {
		return activity;
	}
	public long calculateBalance(long balance) {
		return expression.apply(balance);
	}
	
	public static TransferType findByCode(String code) {
		return Arrays.stream(values())
				.filter(p -> p.getCode().equals(code))
				.findAny()
				.orElseThrow(UnsupportedOperationException::new);
	}
	
	public static TransferType findByActivity(Activity activity) {
		return Arrays.stream(values())
				.filter(p -> p.getActivity().equals(activity))
				.findAny()
				.orElseThrow(UnsupportedOperationException::new);
	}
	
}
