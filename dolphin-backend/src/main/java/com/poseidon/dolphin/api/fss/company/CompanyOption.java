package com.poseidon.dolphin.api.fss.company;

import javax.persistence.Convert;
import javax.persistence.Embeddable;

import com.poseidon.dolphin.api.fss.company.converter.AreaConverter;
import com.poseidon.dolphin.api.fss.company.json.FSSCompanyOption;

@Embeddable
public class CompanyOption {
	private String disclosureMonth;
	private String financeCompanyNumber;
	@Convert(converter=AreaConverter.class)
	private Area area;
	private String areaName;
	private String existsYn;
	public String getDisclosureMonth() {
		return disclosureMonth;
	}
	public void setDisclosureMonth(String disclosureMonth) {
		this.disclosureMonth = disclosureMonth;
	}
	public String getFinanceCompanyNumber() {
		return financeCompanyNumber;
	}
	public void setFinanceCompanyNumber(String financeCompanyNumber) {
		this.financeCompanyNumber = financeCompanyNumber;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getExistsYn() {
		return existsYn;
	}
	public void setExistsYn(String existsYn) {
		this.existsYn = existsYn;
	}
	
	public static CompanyOption from(FSSCompanyOption fssCompanyOption) {
		CompanyOption companyOption = new CompanyOption();
		companyOption.setDisclosureMonth(fssCompanyOption.getDcls_month());
		companyOption.setFinanceCompanyNumber(fssCompanyOption.getFin_co_no());
		companyOption.setArea(Area.findByAreaCode(fssCompanyOption.getArea_cd()));
		companyOption.setAreaName(fssCompanyOption.getArea_nm());
		companyOption.setExistsYn(fssCompanyOption.getExis_yn());
		return companyOption;
	}
	
	@Override
	public String toString() {
		return "CompanyOption [disclosureMonth=" + disclosureMonth + ", financeCompanyNumber=" + financeCompanyNumber
				+ ", area=" + area + ", areaName=" + areaName + ", existsYn=" + existsYn + "]";
	}

}
