package com.poseidon.dolphin.api.fss.saving;

import javax.persistence.Convert;
import javax.persistence.Embeddable;

import com.poseidon.dolphin.api.fss.common.FSSProductOption;
import com.poseidon.dolphin.api.fss.common.InterestRateType;
import com.poseidon.dolphin.api.fss.common.ReserveType;
import com.poseidon.dolphin.api.fss.common.converter.InterestRateTypeConverter;
import com.poseidon.dolphin.api.fss.common.converter.ReserveTypeConverter;

@Embeddable
public class SavingOption {
	private String disclosureMonth;
	private String financeCompanyNumber;
	private String financeProductCode;
	@Convert(converter=InterestRateTypeConverter.class)
	private InterestRateType interestRateType;
	private String interestRateTypeName;
	@Convert(converter=ReserveTypeConverter.class)
	private ReserveType reserveType;
	private String reserveTypeName;
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
	public ReserveType getReserveType() {
		return reserveType;
	}
	public void setReserveType(ReserveType reserveType) {
		this.reserveType = reserveType;
	}
	public String getReserveTypeName() {
		return reserveTypeName;
	}
	public void setReserveTypeName(String reserveTypeName) {
		this.reserveTypeName = reserveTypeName;
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
	
	public static SavingOption from(FSSProductOption fssProductOption) {
		SavingOption savingOption = new SavingOption();
		savingOption.setDisclosureMonth(fssProductOption.getDcls_month());
		savingOption.setFinanceCompanyNumber(fssProductOption.getFin_co_no());
		savingOption.setFinanceProductCode(fssProductOption.getFin_prdt_cd());
		savingOption.setInterestRateType(InterestRateType.findByCode(fssProductOption.getIntr_rate_type()));
		savingOption.setInterestRateTypeName(fssProductOption.getIntr_rate_type_nm());
		savingOption.setReserveType(ReserveType.findByCode(fssProductOption.getRsrv_type()));
		savingOption.setReserveTypeName(fssProductOption.getRsrv_type_nm());
		savingOption.setSaveTerm(fssProductOption.getSave_trm());
		savingOption.setInterestRate(fssProductOption.getIntr_rate());
		savingOption.setPrimeRate(fssProductOption.getIntr_rate2());
		return savingOption;
	}
	
	@Override
	public String toString() {
		return "SavingOption [disclosureMonth=" + disclosureMonth + ", financeCompanyNumber=" + financeCompanyNumber
				+ ", financeProductCode=" + financeProductCode + ", interestRateType=" + interestRateType
				+ ", interestRateTypeName=" + interestRateTypeName + ", reserveType=" + reserveType
				+ ", reserveTypeName=" + reserveTypeName + ", saveTerm=" + saveTerm + ", interestRate=" + interestRate
				+ ", primeRate=" + primeRate + "]";
	}
	
}
