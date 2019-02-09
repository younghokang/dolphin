package com.poseidon.dolphin.api.fss.common;

public class FSSProduct {
	/**
	 * 공시 제출월[YYYYMM]
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
	 * 금융회사 명
	 */
	private String kor_co_nm;
	/**
	 * 금융상품명
	 */
	private String fin_prdt_nm;
	/**
	 * 가입방법
	 */
	private String join_way;
	/**
	 * 만기 후 이자율
	 */
	private String mtrt_int;
	/**
	 * 우대조건
	 */
	private String spcl_cnd;
	/**
	 * 가입제한
		Ex) 1:제한없음, 2:서민전용, 3:일부제한
	 */
	private int join_deny;
	/**
	 * 가입대상
	 */
	private String join_member;
	/**
	 * 기타 유의사항
	 */
	private String etc_note;
	/**
	 * 최고한도
	 */
	private long max_limit;
	/**
	 * 공시 시작일(yyyyMMdd)
	 */
	private String dcls_strt_day;
	/**
	 * 공시 종료일
	 */
	private String dcls_end_day;
	/**
	 * 금융회사 제출일 [YYYYMMDDHH24MI]
	 */
	private String fin_co_subm_day;
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
	public String getKor_co_nm() {
		return kor_co_nm;
	}
	public void setKor_co_nm(String kor_co_nm) {
		this.kor_co_nm = kor_co_nm;
	}
	public String getFin_prdt_nm() {
		return fin_prdt_nm;
	}
	public void setFin_prdt_nm(String fin_prdt_nm) {
		this.fin_prdt_nm = fin_prdt_nm;
	}
	public String getJoin_way() {
		return join_way;
	}
	public void setJoin_way(String join_way) {
		this.join_way = join_way;
	}
	public String getMtrt_int() {
		return mtrt_int;
	}
	public void setMtrt_int(String mtrt_int) {
		this.mtrt_int = mtrt_int;
	}
	public String getSpcl_cnd() {
		return spcl_cnd;
	}
	public void setSpcl_cnd(String spcl_cnd) {
		this.spcl_cnd = spcl_cnd;
	}
	public int getJoin_deny() {
		return join_deny;
	}
	public void setJoin_deny(int join_deny) {
		this.join_deny = join_deny;
	}
	public String getJoin_member() {
		return join_member;
	}
	public void setJoin_member(String join_member) {
		this.join_member = join_member;
	}
	public String getEtc_note() {
		return etc_note;
	}
	public void setEtc_note(String etc_note) {
		this.etc_note = etc_note;
	}
	public long getMax_limit() {
		return max_limit;
	}
	public void setMax_limit(long max_limit) {
		this.max_limit = max_limit;
	}
	public String getDcls_strt_day() {
		return dcls_strt_day;
	}
	public void setDcls_strt_day(String dcls_strt_day) {
		this.dcls_strt_day = dcls_strt_day;
	}
	public String getDcls_end_day() {
		return dcls_end_day;
	}
	public void setDcls_end_day(String dcls_end_day) {
		this.dcls_end_day = dcls_end_day;
	}
	public String getFin_co_subm_day() {
		return fin_co_subm_day;
	}
	public void setFin_co_subm_day(String fin_co_subm_day) {
		this.fin_co_subm_day = fin_co_subm_day;
	}
	@Override
	public String toString() {
		return "FSSProduct [dcls_month=" + dcls_month + ", fin_co_no=" + fin_co_no + ", fin_prdt_cd=" + fin_prdt_cd
				+ ", kor_co_nm=" + kor_co_nm + ", fin_prdt_nm=" + fin_prdt_nm + ", join_way=" + join_way + ", mtrt_int="
				+ mtrt_int + ", spcl_cnd=" + spcl_cnd + ", join_deny=" + join_deny + ", join_member=" + join_member
				+ ", etc_note=" + etc_note + ", max_limit=" + max_limit + ", dcls_strt_day=" + dcls_strt_day
				+ ", dcls_end_day=" + dcls_end_day + ", fin_co_subm_day=" + fin_co_subm_day + "]";
	}
}
