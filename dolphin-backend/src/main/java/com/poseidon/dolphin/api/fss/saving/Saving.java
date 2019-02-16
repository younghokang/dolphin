package com.poseidon.dolphin.api.fss.saving;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import com.poseidon.dolphin.api.fss.common.FSSProduct;
import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.common.JoinDeny;
import com.poseidon.dolphin.api.fss.common.converter.FinanceGroupConverter;
import com.poseidon.dolphin.api.fss.common.converter.JoinDenyConverter;

@Entity
@Table(name="FSSSaving")
@EntityListeners(AuditingEntityListener.class)
public class Saving {
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
	private String financeProductCode;
	private String koreanFinanceCompanyName;
	private String financeProductName;
	private String joinWay;
	private String maturityInterest;
	private String specialCondition;
	@Convert(converter=JoinDenyConverter.class)
	private JoinDeny joinDeny;
	private String joinMember;
	private String etcNote;
	private Long maxLimit;
	private LocalDate disclosureStartDay;
	private LocalDate disclosureEndDay;
	private LocalDateTime financeCompanySubmitDay;
	
	@ElementCollection
	@CollectionTable(name="FSSSavingOptions", joinColumns=@JoinColumn(name="fssSavingId"))
	private List<SavingOption> savingOptions = new ArrayList<>();
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
	public String getFinanceProductCode() {
		return financeProductCode;
	}
	public void setFinanceProductCode(String financeProductCode) {
		this.financeProductCode = financeProductCode;
	}
	public String getKoreanFinanceCompanyName() {
		return koreanFinanceCompanyName;
	}
	public void setKoreanFinanceCompanyName(String koreanFinanceCompanyName) {
		this.koreanFinanceCompanyName = koreanFinanceCompanyName;
	}
	public String getFinanceProductName() {
		return financeProductName;
	}
	public void setFinanceProductName(String financeProductName) {
		this.financeProductName = financeProductName;
	}
	public String getJoinWay() {
		return joinWay;
	}
	public void setJoinWay(String joinWay) {
		this.joinWay = joinWay;
	}
	public String getMaturityInterest() {
		return maturityInterest;
	}
	public void setMaturityInterest(String maturityInterest) {
		this.maturityInterest = maturityInterest;
	}
	public String getSpecialCondition() {
		return specialCondition;
	}
	public void setSpecialCondition(String specialCondition) {
		this.specialCondition = specialCondition;
	}
	public JoinDeny getJoinDeny() {
		return joinDeny;
	}
	public void setJoinDeny(JoinDeny joinDeny) {
		this.joinDeny = joinDeny;
	}
	public String getJoinMember() {
		return joinMember;
	}
	public void setJoinMember(String joinMember) {
		this.joinMember = joinMember;
	}
	public String getEtcNote() {
		return etcNote;
	}
	public void setEtcNote(String etcNote) {
		this.etcNote = etcNote;
	}
	public Long getMaxLimit() {
		return maxLimit;
	}
	public void setMaxLimit(Long maxLimit) {
		this.maxLimit = maxLimit;
	}
	public LocalDate getDisclosureStartDay() {
		return disclosureStartDay;
	}
	public void setDisclosureStartDay(LocalDate disclosureStartDay) {
		this.disclosureStartDay = disclosureStartDay;
	}
	public LocalDate getDisclosureEndDay() {
		return disclosureEndDay;
	}
	public void setDisclosureEndDay(LocalDate disclosureEndDay) {
		this.disclosureEndDay = disclosureEndDay;
	}
	public LocalDateTime getFinanceCompanySubmitDay() {
		return financeCompanySubmitDay;
	}
	public void setFinanceCompanySubmitDay(LocalDateTime financeCompanySubmitDay) {
		this.financeCompanySubmitDay = financeCompanySubmitDay;
	}
	public List<SavingOption> getSavingOptions() {
		return savingOptions;
	}
	public void setSavingOptions(List<SavingOption> savingOptions) {
		this.savingOptions = savingOptions;
	}
	
	public static Saving from(FSSProduct fssProduct) {
		Saving saving = new Saving();
		saving.setDisclosureMonth(fssProduct.getDcls_month());
		saving.setFinanceCompanyNumber(fssProduct.getFin_co_no());
		saving.setFinanceProductCode(fssProduct.getFin_prdt_cd());
		saving.setKoreanFinanceCompanyName(fssProduct.getKor_co_nm());
		saving.setFinanceProductName(fssProduct.getFin_prdt_nm());
		saving.setJoinWay(fssProduct.getJoin_way());
		saving.setMaturityInterest(fssProduct.getMtrt_int());
		saving.setSpecialCondition(fssProduct.getSpcl_cnd());
		saving.setJoinDeny(JoinDeny.findByCode(fssProduct.getJoin_deny()));
		saving.setJoinMember(fssProduct.getJoin_member());
		saving.setEtcNote(fssProduct.getEtc_note());
		saving.setMaxLimit(fssProduct.getMax_limit());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		if(!StringUtils.isEmpty(fssProduct.getDcls_strt_day())) {
			saving.setDisclosureStartDay(LocalDate.parse(fssProduct.getDcls_strt_day(), formatter));
		}
		if(!StringUtils.isEmpty(fssProduct.getDcls_end_day())) {
			saving.setDisclosureEndDay(LocalDate.parse(fssProduct.getDcls_end_day(), formatter));
		}
		saving.setFinanceCompanySubmitDay(LocalDateTime.parse(fssProduct.getFin_co_subm_day(), DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
		return saving;
	}
	
	@Override
	public String toString() {
		return "Saving [id=" + id + ", createdDate=" + createdDate + ", lastModifiedDate=" + lastModifiedDate
				+ ", financeGroup=" + financeGroup + ", disclosureMonth=" + disclosureMonth + ", financeCompanyNumber="
				+ financeCompanyNumber + ", financeProductCode=" + financeProductCode + ", koreanFinanceCompanyName="
				+ koreanFinanceCompanyName + ", financeProductName=" + financeProductName + ", joinWay=" + joinWay
				+ ", maturityInterest=" + maturityInterest + ", specialCondition=" + specialCondition + ", joinDeny="
				+ joinDeny + ", joinMember=" + joinMember + ", etcNote=" + etcNote + ", maxLimit=" + maxLimit
				+ ", disclosureStartDay=" + disclosureStartDay + ", disclosureEndDay=" + disclosureEndDay
				+ ", financeCompanySubmitDay=" + financeCompanySubmitDay + ", savingOptions=" + savingOptions + "]";
	}
	
}
