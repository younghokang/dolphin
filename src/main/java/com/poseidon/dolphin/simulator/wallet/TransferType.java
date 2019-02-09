package com.poseidon.dolphin.simulator.wallet;

import java.util.Arrays;

import com.poseidon.dolphin.simulator.timeline.Activity;

/**
 * @author gang-yeongho
 * 이체 유형
 */
public enum TransferType {
	/**
	 * 적금 해지
	 */
	INSTALLMENT_SAVING_CLOSE("ISC", Activity.INSTALLMENT_SAVING_ACCOUNT_CLOSE),
	/**
	 * 예금 해지
	 */
	FIXED_DEPOSIT_CLOSE("FDC", Activity.FIXED_DEPOSIT_ACCOUNT_CLOSE),
	/**
	 * 예금 개설
	 */
	FIXED_DEPOSIT_OPEN("FDO", Activity.FIXED_DEPOSIT_ACCOUNT_OPEN);
	
	private final String code;
	private final Activity activity;
	TransferType(String code, Activity activity) {
		this.code = code;
		this.activity = activity;
	}
	
	public String getCode() {
		return code;
	}
	public Activity getActivity() {
		return activity;
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
