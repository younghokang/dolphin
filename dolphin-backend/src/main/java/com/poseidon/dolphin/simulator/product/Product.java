package com.poseidon.dolphin.simulator.product;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.common.InterestRateType;
import com.poseidon.dolphin.api.fss.common.JoinDeny;
import com.poseidon.dolphin.api.fss.common.ReserveType;
import com.poseidon.dolphin.api.fss.common.converter.FinanceGroupConverter;
import com.poseidon.dolphin.api.fss.common.converter.JoinDenyConverter;
import com.poseidon.dolphin.api.fss.common.converter.ProductTypeConverter;
import com.poseidon.dolphin.api.fss.deposit.Deposit;
import com.poseidon.dolphin.api.fss.deposit.DepositOption;
import com.poseidon.dolphin.api.fss.saving.Saving;
import com.poseidon.dolphin.api.fss.saving.SavingOption;
import com.poseidon.dolphin.simulator.TestState;
import com.poseidon.dolphin.simulator.company.FinanceCompany;

/**
 * @author gang-yeongho
 * 가입기간: 최소 1개월, 최대 12개월, (일,월단위로 지정 가능 / 월단위)
	가입금액: 최소, 최대
	이자지급방식: 만기일지급식/매월이자지급
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Product {
	public static final long MAX_DEPOSIT_PROTECTION_BALANCE = 50000000l;
	public static final long DEFAULT_MIN_BALANCE = 100000l;
	public static final int DEFAULT_PERIOD = 12;
	public static final ChronoUnit DEFAULT_PERIOD_UNIT = ChronoUnit.MONTHS;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@CreatedDate
	private LocalDateTime createdDate;
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
	@Convert(converter=ProductTypeConverter.class)
	private ProductType productType;
	@Convert(converter=FinanceGroupConverter.class)
	private FinanceGroup financeGroup;
	@OneToOne
	@JoinColumn(name="financeCompanyId")
	private FinanceCompany financeCompany;
	/**
	 * 공시제출월[YYYYMM]
	 */
	private String disclosureMonth;
	/**
	 * 금융회사 번호
	 */
	private String companyNumber;
	/**
	 * 금융회사 명
	 */
	private String companyName;
	/**
	 * 금융 상품코드
	 */
	private String code;
	/**
	 * 금융 상품명
	 */
	private String name;
	/**
	 * 가입방법
	 */
	private String joinWay;
	/**
	 * 만기 후 이자
	 */
	private String maturityInterest;
	/**
	 * 우대조건
	 */
	private String specialCondition;
	/**
	 * 가입제한
	 */
	@Convert(converter=JoinDenyConverter.class)
	private JoinDeny joinDeny;
	/**
	 * 가입대상
	 */
	private String joinMember;
	/**
	 * 기타 유의사항
	 */
	private String etcNote;
	/**
	 * 최소 가입 나이(만)
	 */
	private int minAge;
	/**
	 * 최대 가입 나이(만)
	 */
	private int maxAge;
	/**
	 * 예금자보호 상품 여부
	 */
	private boolean depositProtect;
	/**
	 * 비과세저축 상품 여부
	 */
	private boolean nonTaxable;
	/**
	 * 최소 가입기간
	 */
	private int minPeriod;
	/**
	 * 최대 가입기간
	 */
	private int maxPeriod;
	/**
	 * 가입기간 단위(일/월/년)
	 */
	@Enumerated(EnumType.STRING)
	private ChronoUnit periodUnit;
	/**
	 * 최소 가입금액
	 */
	private long minBalance;
	/**
	 * 최대 가입금액
	 */
	private long maxBalance;
	/**
	 * 이자 지급방식
	 * 만기일지급식/매월이자지급
	 */
	@Enumerated(EnumType.STRING)
	private InterestPaymentType interestPaymentType;
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	@OrderBy("interestRateType ASC, reserveType ASC")
	private List<ProductOption> productOptions = new ArrayList<>();
	@Enumerated(EnumType.STRING)
	private TestState testState;
	@Enumerated(EnumType.STRING)
	private State state;
	@Transient
	private long interestBeforeTax;
	@Transient
	private long totalPrincipal;
	@Transient
	private long interestIncomeTax;
	@Transient
	private long afterPayingTax;
	@Transient
	private Long filteredOptionId;
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
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	public FinanceGroup getFinanceGroup() {
		return financeGroup;
	}
	public void setFinanceGroup(FinanceGroup financeGroup) {
		this.financeGroup = financeGroup;
	}
	public FinanceCompany getFinanceCompany() {
		return financeCompany;
	}
	public void setFinanceCompany(FinanceCompany financeCompany) {
		this.financeCompany = financeCompany;
	}
	public String getDisclosureMonth() {
		return disclosureMonth;
	}
	public void setDisclosureMonth(String disclosureMonth) {
		this.disclosureMonth = disclosureMonth;
	}
	public String getCompanyNumber() {
		return companyNumber;
	}
	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public long getInterestBeforeTax() {
		return interestBeforeTax;
	}
	public void setInterestBeforeTax(long interestBeforeTax) {
		this.interestBeforeTax = interestBeforeTax;
	}
	public long getTotalPrincipal() {
		return totalPrincipal;
	}
	public void setTotalPrincipal(long totalPrincipal) {
		this.totalPrincipal = totalPrincipal;
	}
	public long getInterestIncomeTax() {
		return interestIncomeTax;
	}
	public void setInterestIncomeTax(long interestIncomeTax) {
		this.interestIncomeTax = interestIncomeTax;
	}
	public long getAfterPayingTax() {
		return afterPayingTax;
	}
	public void setAfterPayingTax(long afterPayingTax) {
		this.afterPayingTax = afterPayingTax;
	}
	public Long getFilteredOptionId() {
		return filteredOptionId;
	}
	public void setFilteredOptionId(Long filteredOptionId) {
		this.filteredOptionId = filteredOptionId;
	}

	public boolean availablePeriod(int period) {
		return period >= getMinPeriod() && period <= getMaxPeriod();
	}
	
	public boolean availableBalance(long balance) {
		long max = getMaxBalance() == 0 ? Long.MAX_VALUE : getMaxBalance();
		return balance >= getMinBalance() && balance <= max;
	}
	
	public boolean availableAge(int age) {
		return age >= getMinAge() && age <= getMaxAge();
	}
	
	public static Product from(Saving s) {
		Product p = new Product();
		p.setProductType(ProductType.INSTALLMENT_SAVING);
		p.setFinanceGroup(s.getFinanceGroup());
		p.setDisclosureMonth(s.getDisclosureMonth());
		p.setCompanyNumber(s.getFinanceCompanyNumber());
		p.setCompanyName(s.getKoreanFinanceCompanyName());
		p.setCode(s.getFinanceProductCode());
		p.setName(s.getFinanceProductName());
		p.setJoinWay(s.getJoinWay());
		p.setMaturityInterest(s.getMaturityInterest());
		p.setSpecialCondition(s.getSpecialCondition());
		p.setJoinDeny(s.getJoinDeny());
		p.setJoinMember(s.getJoinMember());
		p.setEtcNote(s.getEtcNote());
		p.setMaxBalance(s.getMaxLimit() == null ? 0 : s.getMaxLimit().longValue());
		p.setPeriodUnit(ChronoUnit.MONTHS);
		p.setMinPeriod(s.getSavingOptions().stream()
			.mapToInt(SavingOption::getSaveTerm)
			.min()
			.orElse(0));
		p.setMaxPeriod(s.getSavingOptions().stream()
			.mapToInt(SavingOption::getSaveTerm)
			.max()
			.orElse(0));
		
		List<ProductOption> productOptions = new ArrayList<>();
		s.getSavingOptions().stream()
			.collect(Collectors.groupingBy(SavingOption::getInterestRateType, Collectors.groupingBy(SavingOption::getReserveType)))
			.entrySet().stream()
			.forEach(entry -> {
				InterestRateType interestRateType = entry.getKey();
				List<ProductOption> mappedProductOptions = entry.getValue().entrySet().stream()
					.map(reserveTypeMap -> {
						ProductOption productOption = new ProductOption();
						productOption.setInterestRateType(interestRateType);
						productOption.setReserveType(reserveTypeMap.getKey());
						productOption.getInterestRates().addAll(
								reserveTypeMap.getValue().stream()
									.map(InterestRate::from)
									.collect(Collectors.toList()));
						return productOption;
					})
					.sorted(Comparator.comparing(ProductOption::getReserveType))
					.collect(Collectors.toList());
				productOptions.addAll(mappedProductOptions);
			});
		
		p.getProductOptions().addAll(productOptions);
		p.setTestState(TestState.READY);
		p.setState(State.OPEN);
		return p;
	}
	
	public static Product from(Deposit d) {
		Product p = new Product();
		p.setProductType(ProductType.FIXED_DEPOSIT);
		p.setFinanceGroup(d.getFinanceGroup());
		p.setDisclosureMonth(d.getDisclosureMonth());
		p.setCompanyNumber(d.getFinanceCompanyNumber());
		p.setCompanyName(d.getKoreanFinanceCompanyName());
		p.setCode(d.getFinanceProductCode());
		p.setName(d.getFinanceProductName());
		p.setJoinWay(d.getJoinWay());
		p.setMaturityInterest(d.getMaturityInterest());
		p.setSpecialCondition(d.getSpecialCondition());
		p.setJoinDeny(d.getJoinDeny());
		p.setJoinMember(d.getJoinMember());
		p.setEtcNote(d.getEtcNote());
		p.setMaxBalance(d.getMaxLimit() == null ? 0 : d.getMaxLimit().longValue());
		p.setPeriodUnit(ChronoUnit.MONTHS);
		p.setMinPeriod(d.getDepositOptions().stream()
				.mapToInt(DepositOption::getSaveTerm)
				.min()
				.orElse(0));
			p.setMaxPeriod(d.getDepositOptions().stream()
				.mapToInt(DepositOption::getSaveTerm)
				.max()
				.orElse(0));
		
		List<ProductOption> productOptions = d.getDepositOptions().stream()
			.collect(Collectors.groupingBy(DepositOption::getInterestRateType))
			.entrySet().stream()
			.map(entry -> {
				ProductOption productOption = new ProductOption();
				productOption.setInterestRateType(entry.getKey());
				productOption.setReserveType(ReserveType.FIXED);
				productOption.getInterestRates().addAll(
						entry.getValue().stream()
						.map(InterestRate::from)
						.collect(Collectors.toList()));
				return productOption;
			})
			.sorted(Comparator.comparing(ProductOption::getInterestRateType))
			.collect(Collectors.toList());
		
		p.getProductOptions().addAll(productOptions);
		p.setTestState(TestState.READY);
		p.setState(State.OPEN);
		return p;
	}
	
	@Override
	public String toString() {
		return "Product [id=" + id + ", createdDate=" + createdDate + ", lastModifiedDate=" + lastModifiedDate
				+ ", productType=" + productType + ", financeGroup=" + financeGroup + ", financeCompany="
				+ financeCompany + ", disclosureMonth=" + disclosureMonth + ", companyNumber=" + companyNumber
				+ ", companyName=" + companyName + ", code=" + code + ", name=" + name + ", joinWay=" + joinWay
				+ ", maturityInterest=" + maturityInterest + ", specialCondition=" + specialCondition + ", joinDeny="
				+ joinDeny + ", joinMember=" + joinMember + ", etcNote=" + etcNote + ", minAge=" + minAge + ", maxAge="
				+ maxAge + ", depositProtect=" + depositProtect + ", nonTaxable=" + nonTaxable + ", minPeriod="
				+ minPeriod + ", maxPeriod=" + maxPeriod + ", periodUnit=" + periodUnit + ", minBalance=" + minBalance
				+ ", maxBalance=" + maxBalance + ", interestPaymentType=" + interestPaymentType + ", options=" + productOptions
				+ ", testState=" + testState + ", state=" + state + ", interestBeforeTax=" + interestBeforeTax
				+ ", totalPrincipal=" + totalPrincipal + ", interestIncomeTax=" + interestIncomeTax
				+ ", afterPayingTax=" + afterPayingTax + ", filteredOptionId=" + filteredOptionId + "]";
	}
	
}
