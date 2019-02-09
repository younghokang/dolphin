package com.poseidon.dolphin.api.fss.deposit;

import javax.persistence.Convert;
import javax.persistence.Embeddable;

import com.poseidon.dolphin.api.fss.common.FSSProductOption;
import com.poseidon.dolphin.api.fss.common.InterestRateType;
import com.poseidon.dolphin.api.fss.common.converter.InterestRateTypeConverter;

@Embeddable
public class DepositOption {
	private String disclosureMonth;
	private String financeCompanyNumber;
	private String financeProductCode;
	@Convert(converter=InterestRateTypeConverter.class)
	private InterestRateType interestRateType;
	private String interestRateTypeName;
	private Integer saveTerm;
	private Double interestRate;
	private Double primeRate;
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
	public String getFinanceProductCode() {
		return financeProductCode;
	}
	public void setFinanceProductCode(String financeProductCode) {
		this.financeProductCode = financeProductCode;
	}
	public InterestRateType getInterestRateType() {
		return interestRateType;
	}
	public void setInterestRateType(InterestRateType interestRateType) {
		this.interestRateType = interestRateType;
	}
	public String getInterestRateTypeName() {
		return interestRateTypeName;
	}
	public void setInterestRateTypeName(String interestRateTypeName) {
		this.interestRateTypeName = interestRateTypeName;
	}
	public Integer getSaveTerm() {
		return saveTerm;
	}
	public void setSaveTerm(Integer saveTerm) {
		this.saveTerm = saveTerm;
	}
	public Double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}
	public Double getPrimeRate() {
		return primeRate;
	}
	public void setPrimeRate(Double primeRate) {
		this.primeRate = primeRate;
	}
	
	public static DepositOption from(FSSProductOption fssProductOption) {
		DepositOption depositOption = new DepositOption();
		depositOption.setDisclosureMonth(fssProductOption.getDcls_month());
		depositOption.setFinanceCompanyNumber(fssProductOption.getFin_co_no());
		depositOption.setFinanceProductCode(fssProductOption.getFin_prdt_cd());
		depositOption.setInterestRateType(InterestRateType.findByCode(fssProductOption.getIntr_rate_type()));
		depositOption.setInterestRateTypeName(fssProductOption.getIntr_rate_type_nm());
		depositOption.setSaveTerm(fssProductOption.getSave_trm());
		depositOption.setInterestRate(fssProductOption.getIntr_rate());
		depositOption.setPrimeRate(fssProductOption.getIntr_rate2());
		return depositOption;
	}
	
	@Override
	public String toString() {
		return "DepositOption [disclosureMonth=" + disclosureMonth + ", financeCompanyNumber=" + financeCompanyNumber
				+ ", financeProductCode=" + financeProductCode + ", interestRateType=" + interestRateType
				+ ", interestRateTypeName=" + interestRateTypeName + ", saveTerm=" + saveTerm + ", interestRate="
				+ interestRate + ", primeRate=" + primeRate + "]";
	}
	
	
}
