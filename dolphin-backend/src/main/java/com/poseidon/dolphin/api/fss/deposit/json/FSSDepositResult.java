package com.poseidon.dolphin.api.fss.deposit.json;

import java.util.List;

import com.poseidon.dolphin.api.fss.common.FSSProduct;
import com.poseidon.dolphin.api.fss.common.FSSProductOption;

public class FSSDepositResult {
	/**
	 * 상품구분
	 */
	private String prdt_div;
	/**
	 * 에러코드
	 */
	private String err_cd;
	/**
	 * 에러메시지
	 */
	private String err_msg;
	/**
	 * 상품건수
	 */
	private int total_count;
	/**
	 * 총 페이지 건수
		(총 페이지 건수 = 총 상품건수/1회 조회 개수*)
	 */
	private int max_page_no;
	/**
	 * 현재 조회 페이지 번호
	 */
	private int now_page_no;
	private List<FSSProduct> baseList;
	private List<FSSProductOption> optionList;
	public String getPrdt_div() {
		return prdt_div;
	}
	public void setPrdt_div(String prdt_div) {
		this.prdt_div = prdt_div;
	}
	public String getErr_cd() {
		return err_cd;
	}
	public void setErr_cd(String err_cd) {
		this.err_cd = err_cd;
	}
	public String getErr_msg() {
		return err_msg;
	}
	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}
	public int getTotal_count() {
		return total_count;
	}
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	public int getMax_page_no() {
		return max_page_no;
	}
	public void setMax_page_no(int max_page_no) {
		this.max_page_no = max_page_no;
	}
	public int getNow_page_no() {
		return now_page_no;
	}
	public void setNow_page_no(int now_page_no) {
		this.now_page_no = now_page_no;
	}
	public List<FSSProduct> getBaseList() {
		return baseList;
	}
	public void setBaseList(List<FSSProduct> baseList) {
		this.baseList = baseList;
	}
	public List<FSSProductOption> getOptionList() {
		return optionList;
	}
	public void setOptionList(List<FSSProductOption> optionList) {
		this.optionList = optionList;
	}
	@Override
	public String toString() {
		return "FSSDepositResult [prdt_div=" + prdt_div + ", err_cd=" + err_cd + ", err_msg=" + err_msg
				+ ", total_count=" + total_count + ", max_page_no=" + max_page_no + ", now_page_no=" + now_page_no
				+ ", baseList=" + baseList + ", optionList=" + optionList + "]";
	}
}
