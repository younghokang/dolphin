package com.poseidon.dolphin.simulator.company;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.poseidon.dolphin.simulator.TestState;
import com.poseidon.dolphin.simulator.product.FinanceGroup;
import com.poseidon.dolphin.simulator.product.converter.FinanceGroupConverter;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class FinanceCompany {
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
	private String code;
	private String name;
	private String disclosureChargeman;
	private String webSite;
	private String tel;
	@ElementCollection
	@CollectionTable(name="FinanceCompanyOptions", joinColumns=@JoinColumn(name="financeCompanyId"))
	private List<FinanceCompanyOption> options = new ArrayList<>();
	@Enumerated(EnumType.STRING)
	private TestState testState;
	
	public FinanceCompany() {}
	
	public FinanceCompany(Long id, LocalDateTime createdDate, LocalDateTime lastModifiedDate, String disclosureMonth,
			String code, String name, String disclosureChargeman, String webSite, String tel,
			List<FinanceCompanyOption> options) {
		super();
		this.id = id;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
		this.disclosureMonth = disclosureMonth;
		this.code = code;
		this.name = name;
		this.disclosureChargeman = disclosureChargeman;
		this.webSite = webSite;
		this.tel = tel;
		this.options = options;
	}
	
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisclosureChargeman() {
		return disclosureChargeman;
	}
	public void setDisclosureChargeman(String disclosureChargeman) {
		this.disclosureChargeman = disclosureChargeman;
	}
	public String getWebSite() {
		return webSite;
	}
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public List<FinanceCompanyOption> getOptions() {
		return options;
	}
	public void setOptions(List<FinanceCompanyOption> options) {
		this.options = options;
	}
	public TestState getTestState() {
		return testState;
	}
	public void setTestState(TestState testState) {
		this.testState = testState;
	}
	
	@Override
	public String toString() {
		return "FinanceCompany [id=" + id + ", createdDate=" + createdDate + ", lastModifiedDate=" + lastModifiedDate
				+ ", disclosureMonth=" + disclosureMonth + ", code=" + code + ", name=" + name
				+ ", disclosureChargeman=" + disclosureChargeman + ", webSite=" + webSite + ", tel=" + tel
				+ ", options=" + options + ", state=" + testState + "]";
	}

}
