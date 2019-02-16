package com.poseidon.dolphin.simulator.filter;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

import com.poseidon.dolphin.simulator.calculator.CalculatorResult;
import com.poseidon.dolphin.simulator.calculator.SimpleCalculator;
import com.poseidon.dolphin.simulator.product.Interest;
import com.poseidon.dolphin.simulator.product.InterestRateType;
import com.poseidon.dolphin.simulator.product.JoinDeny;
import com.poseidon.dolphin.simulator.product.Product;

public class ProductFilter {
	private static final long RECOMMEND_PRODUCTS_MAX_SIZE = 3;
	private static final String[] DEFAULT_EXCLUDE_JOIN_MEMBERS = {
			"기초생활",
			"소년소녀",
			"북한",
			"결혼이민",
			"근로장려금",
			"한부모가족",
			"차상위계층",
			"장애인",
			"비과세종합",
			"국가유공자",
			"독립유공자",
			"고엽제",
			"민주화운동",
			"의무복무",
			"병사",
			"군장병",
			"현역병",
			"상근예비역",
			"의무경찰",
			"해양의무경찰",
			"의무소방원",
			"사회복무요원",
			"다문화가정",
			"쳥년내일채움",
			"군인",
			"경찰",
			"소방관",
			"탈북민"
	};
	
	private final long balance;
	private int period = Product.DEFAULT_PERIOD;
	private ChronoUnit periodUnit = Product.DEFAULT_PERIOD_UNIT;
	private JoinDeny joinDeny = JoinDeny.NONE;
	private Set<String> excludeCompanyNumbers = Collections.emptySet();
	private long size = RECOMMEND_PRODUCTS_MAX_SIZE;
	private String[] excludeJoinMembers = DEFAULT_EXCLUDE_JOIN_MEMBERS;
	
	public ProductFilter(long balance) {
		Assert.isTrue(balance >= Product.DEFAULT_MIN_BALANCE, "balance must be greater than " + Product.DEFAULT_MIN_BALANCE);
		this.balance = balance;
	}

	public long getBalance() {
		return balance;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	public ChronoUnit getPeriodUnit() {
		return periodUnit;
	}
	public void setPeriodUnit(ChronoUnit periodUnit) {
		this.periodUnit = periodUnit;
	}
	public JoinDeny getJoinDeny() {
		return joinDeny;
	}
	public void setJoinDeny(JoinDeny joinDeny) {
		this.joinDeny = joinDeny;
	}
	public Set<String> getExcludeCompanyNumbers() {
		return excludeCompanyNumbers;
	}
	public void setExcludeCompanyNumbers(Set<String> excludeCompanyNumbers) {
		this.excludeCompanyNumbers = Collections.unmodifiableSet(excludeCompanyNumbers);
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String[] getExcludeJoinMembers() {
		return excludeJoinMembers;
	}
	public void setExcludeJoinMembers(String[] excludeJoinMembers) {
		this.excludeJoinMembers = excludeJoinMembers;
	}
	
	public List<Product> filter(List<Product> products) {
		if(products == null) {
			return Collections.emptyList();
		}
		return products.stream()
			.filter(product -> product.availableBalance(balance) && product.availablePeriod(period)
					&& product.getJoinDeny().equals(joinDeny) && !excludeCompanyNumbers.contains(product.getCompanyNumber())
					&& !product.hasJoinMember(excludeJoinMembers))
			.peek(this::filter)
			.sorted(Comparator.comparing(Product::getInterestBeforeTax).reversed())
			.limit(size)
			.collect(Collectors.toList());
	}
	
	public void filter(Product product) {
		CalculatorResult result = product.getProductOptions().stream()
			.map(option -> {
				double rate = option.getInterestRates().stream()
					.filter(ir -> ir.getContractPeriod() == period)
					.map(ir -> {
						return ir.getRate();
					})
					.max(Comparator.naturalOrder())
					.orElse(Double.valueOf(0));
				
				SimpleCalculator calculator = new SimpleCalculator(balance, rate, product.getProductType());
				calculator.setPeriod(period);
				calculator.setChronoUnit(periodUnit);
				if(option.getInterestRateType() == InterestRateType.COMPOUND) {
					calculator.setInterest(Interest.MONTH_COMPOUND);
				}
				calculator.setOptionId(option.getId());
				return calculator.calculate();
			})
			.max(Comparator.comparing(CalculatorResult::getInterestBeforeTax))
			.orElse(null);
		if(result != null) {
			product.setTotalPrincipal(result.getTotalPrincipal());
			product.setInterestBeforeTax(result.getInterestBeforeTax());
			product.setInterestIncomeTax(result.getInterestIncomeTax());
			product.setAfterPayingTax(result.getTotalPrincipal() + result.getInterestBeforeTax() - result.getInterestIncomeTax());
			product.setFilteredOptionId(result.getOptionId());
		}
	}
	
}
