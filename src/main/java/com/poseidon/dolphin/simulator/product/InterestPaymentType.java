package com.poseidon.dolphin.simulator.product;

/**
 * @author gang-yeongho
 * 이자지급방식: 만기일지급식/매월이자지급
 */
public enum InterestPaymentType {
	/**
	 * 매월이자지급
	 */
	MONTHLY_PAYMENT, 
	/**
	 * 만기일지급식
	 */
	ALL_DUE_DATE_PAYMENT,
	/**
	 * 연간이자지급
	 */
	YEARLY_PAYMENT
}
