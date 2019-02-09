package com.poseidon.dolphin.simulator.product;

import java.time.temporal.ChronoUnit;
import java.util.List;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.common.JoinDeny;
import com.poseidon.dolphin.simulator.TestState;

public class ProductCommand {
	private long id;
	private String name;
	private String companyName;
	private String code;
	private String disclosureMonth;
	private FinanceGroup financeGroup;
	private String joinWay;
	private String maturityInterest;
	private String specialCondition;
	private String joinMember;
	private JoinDeny joinDeny;
	private String etcNote;
	private int minAge;
	private int maxAge;
	private boolean depositProtect;
	private boolean nonTaxable;
	private int minPeriod;
	private int maxPeriod;
	private ChronoUnit periodUnit;
	private long minBalance;
	private long maxBalance;
	private InterestPaymentType interestPaymentType;
	private List<ProductOption> productOptions;
	private TestState testState;
	private State state;
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
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDisclosureMonth() {
		return disclosureMonth;
	}
	public void setDisclosureMonth(String disclosureMonth) {
		this.disclosureMonth = disclosureMonth;
	}
	public FinanceGroup getFinanceGroup() {
		return financeGroup;
	}
	public void setFinanceGroup(FinanceGroup financeGroup) {
		this.financeGroup = financeGroup;
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
	public String getJoinMember() {
		return joinMember;
	}
	public void setJoinMember(String joinMember) {
		this.joinMember = joinMember;
	}
	public JoinDeny getJoinDeny() {
		return joinDeny;
	}
	public void setJoinDeny(JoinDeny joinDeny) {
		this.joinDeny = joinDeny;
	}
	public String getEtcNote() {
		return etcNote;
	}
	public void setEtcNote(String etcNote) {
		this.etcNote = etcNote;
	}
	public int getMinAge() {
		return minAge;
	}
	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}
	public int getMaxAge() {
		return maxAge;
	}
	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}
	public boolean isDepositProtect() {
		return depositProtect;
	}
	public void setDepositProtect(boolean depositProtect) {
		this.depositProtect = depositProtect;
	}
	public boolean isNonTaxable() {
		return nonTaxable;
	}
	public void setNonTaxable(boolean nonTaxable) {
		this.nonTaxable = nonTaxable;
	}
	public int getMinPeriod() {
		return minPeriod;
	}
	public void setMinPeriod(int minPeriod) {
		this.minPeriod = minPeriod;
	}
	public int getMaxPeriod() {
		return maxPeriod;
	}
	public void setMaxPeriod(int maxPeriod) {
		this.maxPeriod = maxPeriod;
	}
	public ChronoUnit getPeriodUnit() {
		return periodUnit;
	}
	public void setPeriodUnit(ChronoUnit periodUnit) {
		this.periodUnit = periodUnit;
	}
	public long getMinBalance() {
		return minBalance;
	}
	public void setMinBalance(long minBalance) {
		this.minBalance = minBalance;
	}
	public long getMaxBalance() {
		return maxBalance;
	}
	public void setMaxBalance(long maxBalance) {
		this.maxBalance = maxBalance;
	}
	public InterestPaymentType getInterestPaymentType() {
		return interestPaymentType;
	}
	public void setInterestPaymentType(InterestPaymentType interestPaymentType) {
		this.interestPaymentType = interestPaymentType;
	}
	public List<ProductOption> getProductOptions() {
		return productOptions;
	}
	public void setProductOptions(List<ProductOption> productOptions) {
		this.productOptions = productOptions;
	}
	public TestState getTestState() {
		return testState;
	}
	public void setTestState(TestState testState) {
		this.testState = testState;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public static ProductCommand from(Product product) {
		ProductCommand pc = new ProductCommand();
		pc.setId(product.getId());
		pc.setName(product.getName());
		pc.setCompanyName(product.getCompanyName());
		pc.setDisclosureMonth(product.getDisclosureMonth());
		pc.setCode(product.getCode());
		pc.setJoinWay(product.getJoinWay());
		pc.setMaturityInterest(product.getMaturityInterest());
		pc.setJoinMember(product.getJoinMember());
		pc.setSpecialCondition(product.getSpecialCondition());
		pc.setJoinDeny(product.getJoinDeny());
		pc.setFinanceGroup(product.getFinanceGroup());
		pc.setEtcNote(product.getEtcNote());
		pc.setMinAge(product.getMinAge());
		pc.setMaxAge(product.getMaxAge());
		pc.setDepositProtect(product.isDepositProtect());
		pc.setNonTaxable(product.isNonTaxable());
		pc.setMinPeriod(product.getMinPeriod());
		pc.setMaxPeriod(product.getMaxPeriod());
		pc.setPeriodUnit(product.getPeriodUnit());
		pc.setMinBalance(product.getMinBalance());
		pc.setMaxBalance(product.getMaxBalance());
		pc.setInterestPaymentType(product.getInterestPaymentType());
		pc.setProductOptions(product.getProductOptions());
		pc.setTestState(product.getTestState());
		pc.setState(product.getState());
		return pc;
	}
}
