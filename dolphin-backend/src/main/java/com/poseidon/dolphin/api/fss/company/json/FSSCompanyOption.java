package com.poseidon.dolphin.api.fss.company.json;

public class FSSCompanyOption {
	/**
	 * 공시 제출월[YYYYMM]
	 */
	private String dcls_month;
	/**
	 * 금융회사 코드
	 */
	private String fin_co_no;
	/**
	 * 지역구분
	 */
	private String area_cd;
	/**
	 * 지역이름
	 */
	private String area_nm;
	/**
	 * 점포소재여부
	 */
	private String exis_yn;
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
	public String getArea_cd() {
		return area_cd;
	}
	public void setArea_cd(String area_cd) {
		this.area_cd = area_cd;
	}
	public String getArea_nm() {
		return area_nm;
	}
	public void setArea_nm(String area_nm) {
		this.area_nm = area_nm;
	}
	public String getExis_yn() {
		return exis_yn;
	}
	public void setExis_yn(String exis_yn) {
		this.exis_yn = exis_yn;
	}
	@Override
	public String toString() {
		return "FSSCompanyOption [dcls_month=" + dcls_month + ", fin_co_no=" + fin_co_no + ", area_cd=" + area_cd
				+ ", area_nm=" + area_nm + ", exis_yn=" + exis_yn + "]";
	}
}
