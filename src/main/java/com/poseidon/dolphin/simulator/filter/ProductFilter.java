package com.poseidon.dolphin.simulator.filter;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.poseidon.dolphin.simulator.calculator.CalculatorResult;
import com.poseidon.dolphin.simulator.calculator.SimpleCalculator;
import com.poseidon.dolphin.simulator.product.Interest;
import com.poseidon.dolphin.simulator.product.InterestRateType;
import com.poseidon.dolphin.simulator.product.Product;

public class ProductFilter {
	private final long balance;
	private int period = Product.DEFAULT_PERIOD;
	private ChronoUnit periodUnit = Product.DEFAULT_PERIOD_UNIT;
	
	public ProductFilter(long balance) {
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
	
	public List<Product> filter(List<Product> products) {
		return products.stream()
			.filter(product -> product.availableBalance(balance) && product.availablePeriod(period))
			.peek(this::filter)
			.sorted(Comparator.comparing(Product::getInterestBeforeTax).reversed())
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
