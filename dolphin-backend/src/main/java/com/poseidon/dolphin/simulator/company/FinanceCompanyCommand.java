package com.poseidon.dolphin.simulator.company;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.simulator.TestState;

public class FinanceCompanyCommand {
	private long id;
	@Size(min=1, max=30)
	private String name;
	@Size(min=1, max=10)
	private String code;
	private FinanceGroup financeGroup;
	private TestState testState;
	@Size(min=6, max=6)
	private String disclosureMonth;
	private String disclosureChargeman;
	private String webSite;
	private String tel;
	private List<FinanceCompanyOption> options = new ArrayList<>();
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public FinanceGroup getFinanceGroup() {
		return financeGroup;
	}
	public void setFinanceGroup(FinanceGroup financeGroup) {
		this.financeGroup = financeGroup;
	}
	public TestState getTestState() {
		return testState;
	}
	public void setTestState(TestState testState) {
		this.testState = testState;
	}
	public String getDisclosureMonth() {
		return disclosureMonth;
	}
	public void setDisclosureMonth(String disclosureMonth) {
		this.disclosureMonth = disclosureMonth;
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
	
	public static FinanceCompanyCommand from(FinanceCompany fc) {
		FinanceCompanyCommand fcc = new FinanceCompanyCommand();
		fcc.setId(fc.getId());
		fcc.setFinanceGroup(fc.getFinanceGroup());
		fcc.setCode(fc.getCode());
		fcc.setName(fc.getName());
		fcc.setDisclosureChargeman(fc.getDisclosureChargeman());
		fcc.setDisclosureMonth(fc.getDisclosureMonth());
		fcc.setTel(fc.getTel());
		fcc.setWebSite(fc.getWebSite());
		fcc.setTestState(fc.getTestState());
		fcc.getOptions().addAll(fc.getOptions());
		return fcc;
	}
	
}
