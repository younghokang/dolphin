package com.poseidon.dolphin.api.fss.company.json;

public class FSSCompany {
	/**
	 * 공시 제출월[YYYYMM]
	 */
	private String dcls_month;
	/**
	 * 금융회사 코드
	 */
	private String fin_co_no;
	/**
	 * 금융회사 명
	 */
	private String kor_co_nm;
	/**
	 * 공시담당자
	 */
	private String dcls_chrg_man;
	/**
	 * 홈페이지주소
	 */
	private String homp_url;
	/**
	 * 콜센터전화번호
	 */
	private String cal_tel;
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
	public String getKor_co_nm() {
		return kor_co_nm;
	}
	public void setKor_co_nm(String kor_co_nm) {
		this.kor_co_nm = kor_co_nm;
	}
	public String getDcls_chrg_man() {
		return dcls_chrg_man;
	}
	public void setDcls_chrg_man(String dcls_chrg_man) {
		this.dcls_chrg_man = dcls_chrg_man;
	}
	public String getHomp_url() {
		return homp_url;
	}
	public void setHomp_url(String homp_url) {
		this.homp_url = homp_url;
	}
	public String getCal_tel() {
		return cal_tel;
	}
	public void setCal_tel(String cal_tel) {
		this.cal_tel = cal_tel;
	}
	@Override
	public String toString() {
		return "FSSCompany [dcls_month=" + dcls_month + ", fin_co_no=" + fin_co_no + ", kor_co_nm=" + kor_co_nm
				+ ", dcls_chrg_man=" + dcls_chrg_man + ", homp_url=" + homp_url + ", cal_tel=" + cal_tel + "]";
	}
}
