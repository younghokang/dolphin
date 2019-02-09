package com.poseidon.dolphin.api.fss.company;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.common.converter.FinanceGroupConverter;
import com.poseidon.dolphin.api.fss.company.json.FSSCompany;

@Entity
@Table(name="FSSCompany")
@EntityListeners(AuditingEntityListener.class)
public class Company {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@CreatedDate
	private LocalDateTime createdDate;
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
	@Convert(converter=FinanceGroupConverter.class)
	private FinanceGroup financeGroup;
	private String disclosureMonth;
	private String financeCompanyNumber;
	private String koreanFinanceCompanyName;
	private String disclosureChargeMan;
	private String homepageUrl;
	private String callCenterTel;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="FSSCompanyOptions", joinColumns=@JoinColumn(name="fssCompanyId"))
	private List<CompanyOption> companyOptions = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public FinanceGroup getFinanceGroup() {
		return financeGroup;
	}
	public void setFinanceGroup(FinanceGroup financeGroup) {
		this.financeGroup = financeGroup;
	}
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
	public String getKoreanFinanceCompanyName() {
		return koreanFinanceCompanyName;
	}
	public void setKoreanFinanceCompanyName(String koreanFinanceCompanyName) {
		this.koreanFinanceCompanyName = koreanFinanceCompanyName;
	}
	public String getDisclosureChargeMan() {
		return disclosureChargeMan;
	}
	public void setDisclosureChargeMan(String disclosureChargeMan) {
		this.disclosureChargeMan = disclosureChargeMan;
	}
	public String getHomepageUrl() {
		return homepageUrl;
	}
	public void setHomepageUrl(String homepageUrl) {
		this.homepageUrl = homepageUrl;
	}
	public String getCallCenterTel() {
		return callCenterTel;
	}
	public void setCallCenterTel(String callCenterTel) {
		this.callCenterTel = callCenterTel;
	}
	public List<CompanyOption> getCompanyOptions() {
		return companyOptions;
	}
	public void setCompanyOptions(List<CompanyOption> companyOptions) {
		this.companyOptions = companyOptions;
	}
	
	public static Company from(FSSCompany fssCompany) {
		Company company = new Company();
		company.setDisclosureMonth(fssCompany.getDcls_month());
		company.setFinanceCompanyNumber(fssCompany.getFin_co_no());
		company.setKoreanFinanceCompanyName(fssCompany.getKor_co_nm());
		company.setDisclosureChargeMan(fssCompany.getDcls_chrg_man());
		company.setHomepageUrl(fssCompany.getHomp_url());
		company.setCallCenterTel(fssCompany.getCal_tel());
		return company;
	}
	
	@Override
	public String toString() {
		return "Company [id=" + id + ", createdDate=" + createdDate + ", lastModifiedDate=" + lastModifiedDate
				+ ", financeGroup=" + financeGroup + ", disclosureMonth=" + disclosureMonth + ", financeCompanyNumber="
				+ financeCompanyNumber + ", koreanFinanceCompanyName=" + koreanFinanceCompanyName
				+ ", disclosureChargeMan=" + disclosureChargeMan + ", homepageUrl=" + homepageUrl + ", callCenterTel="
				+ callCenterTel + ", companyOptions=" + companyOptions + "]";
	}
	
}
