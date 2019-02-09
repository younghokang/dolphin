package com.poseidon.dolphin.api.fss.common;

public class FSSProductOption {
	/**
	 * 공시 제출월 [YYYYMM]
	 */
	private String dcls_month;
	/**
	 * 금융회사 코드
	 */
	private String fin_co_no;
	/**
	 * 금융상품코드
	 */
	private String fin_prdt_cd;
	/**
	 * 저축 금리 유형
	 */
	private String intr_rate_type;
	/**
	 * 저축 금리 유형명
	 */
	private String intr_rate_type_nm;
	/**
	 * 적립 유형
	 */
	private String rsrv_type;
	/**
	 * 적립 유형명
	 */
	private String rsrv_type_nm;
	/**
	 * 저축 기간 [단위: 개월]
	 */
	private int save_trm;
	/**
	 * 저축 금리 [소수점 2자리]
	 */
	private double intr_rate;
	/**
	 * 최고 우대금리 [소수점 2자리]
	 */
	private double intr_rate2;
	public String getDcls_month() {
		return dcls_month;
	}
	public void setDcls_month(String dcls_month) {
		this.dcls_month = dcls_month;
	}
	public String getFin_co_no() {
		return fin_co_no;
	}
	public void setFin_co_no(String fin_co_no) {
		this.fin_co_no = fin_co_no;
	}
	public String getFin_prdt_cd() {
		return fin_prdt_cd;
	}
	public void setFin_prdt_cd(String fin_prdt_cd) {
		this.fin_prdt_cd = fin_prdt_cd;
	}
	public String getIntr_rate_type() {
		return intr_rate_type;
	}
	public void setIntr_rate_type(String intr_rate_type) {
		this.intr_rate_type = intr_rate_type;
	}
	public String getIntr_rate_type_nm() {
		return intr_rate_type_nm;
	}
	public void setIntr_rate_type_nm(String intr_rate_type_nm) {
		this.intr_rate_type_nm = intr_rate_type_nm;
	}
	public String getRsrv_type() {
		return rsrv_type;
	}
	public void setRsrv_type(String rsrv_type) {
		this.rsrv_type = rsrv_type;
	}
	public String getRsrv_type_nm() {
		return rsrv_type_nm;
	}
	public void setRsrv_type_nm(String rsrv_type_nm) {
		this.rsrv_type_nm = rsrv_type_nm;
	}
	public int getSave_trm() {
		return save_trm;
	}
	public void setSave_trm(int save_trm) {
		this.save_trm = save_trm;
	}
	public double getIntr_rate() {
		return intr_rate;
	}
	public void setIntr_rate(double intr_rate) {
		this.intr_rate = intr_rate;
	}
	public double getIntr_rate2() {
		return intr_rate2;
	}
	public void setIntr_rate2(double intr_rate2) {
		this.intr_rate2 = intr_rate2;
	}
	@Override
	public String toString() {
		return "FSSProductOption [dcls_month=" + dcls_month + ", fin_co_no=" + fin_co_no + ", fin_prdt_cd="
				+ fin_prdt_cd + ", intr_rate_type=" + intr_rate_type + ", intr_rate_type_nm=" + intr_rate_type_nm
				+ ", rsrv_type=" + rsrv_type + ", rsrv_type_nm=" + rsrv_type_nm + ", save_trm=" + save_trm
				+ ", intr_rate=" + intr_rate + ", intr_rate2=" + intr_rate2 + "]";
	}
}
