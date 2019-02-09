package com.poseidon.dolphin.simulator.product.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.dolphin.api.fss.common.InterestRateType;
import com.poseidon.dolphin.api.fss.common.ReserveType;
import com.poseidon.dolphin.api.fss.deposit.Deposit;
import com.poseidon.dolphin.api.fss.deposit.DepositOption;
import com.poseidon.dolphin.api.fss.saving.Saving;
import com.poseidon.dolphin.api.fss.saving.SavingOption;
import com.poseidon.dolphin.simulator.company.repository.FinanceCompanyRepository;
import com.poseidon.dolphin.simulator.product.InterestRate;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductCommand;
import com.poseidon.dolphin.simulator.product.ProductOption;
import com.poseidon.dolphin.simulator.product.repository.ProductRepository;

@Service
public class ProductService {
	private final ProductRepository productRepository;
	private final FinanceCompanyRepository financeCompanyRepository;
	
	@Autowired
	public ProductService(ProductRepository productRepository, FinanceCompanyRepository financeCompanyRepository) {
		this.productRepository = productRepository;
		this.financeCompanyRepository = financeCompanyRepository;
	}
	
	public Product save(ProductCommand productCommand) {
		return productRepository.findById(productCommand.getId()).map(product -> {
			product.setName(productCommand.getName());
			product.setCode(productCommand.getCode());
			product.setCompanyName(productCommand.getCompanyName());
			product.setDisclosureMonth(productCommand.getDisclosureMonth());
			product.setFinanceGroup(productCommand.getFinanceGroup());
			product.setJoinWay(productCommand.getJoinWay());
			product.setMaturityInterest(productCommand.getMaturityInterest());
			product.setSpecialCondition(productCommand.getSpecialCondition());
			product.setJoinMember(productCommand.getJoinMember());
			product.setJoinDeny(productCommand.getJoinDeny());
			product.setEtcNote(productCommand.getEtcNote());
			product.setMinAge(productCommand.getMinAge());
			product.setMaxAge(productCommand.getMaxAge());
			product.setDepositProtect(productCommand.isDepositProtect());
			product.setNonTaxable(productCommand.isNonTaxable());
			product.setMinPeriod(productCommand.getMinPeriod());
			product.setMaxPeriod(productCommand.getMaxPeriod());
			product.setPeriodUnit(productCommand.getPeriodUnit());
			product.setMinBalance(productCommand.getMinBalance());
			product.setMaxBalance(productCommand.getMaxBalance());
			product.setInterestPaymentType(productCommand.getInterestPaymentType());
			product.setTestState(productCommand.getTestState());
			product.setState(productCommand.getState());
			return productRepository.save(product);
		}).orElseThrow(NullPointerException::new);
	}
	
	public List<Product> mergeToSavings(List<Saving> savings) {
		return savings.stream().map(mapper -> {
			return productRepository.findByCodeAndCompanyNumber(mapper.getFinanceProductCode(), mapper.getFinanceCompanyNumber())
					.map(product -> {
						financeCompanyRepository.findByCode(product.getCompanyNumber()).ifPresent(consumer -> {
							product.setFinanceCompany(consumer);
						});
						
						product.setDisclosureMonth(mapper.getDisclosureMonth());
						product.setJoinWay(mapper.getJoinWay());
						product.setMaturityInterest(mapper.getMaturityInterest());
						product.setSpecialCondition(mapper.getSpecialCondition());
						product.setJoinDeny(mapper.getJoinDeny());
						product.setJoinMember(mapper.getJoinMember());
						product.setEtcNote(mapper.getEtcNote());
						product.setMaxBalance(mapper.getMaxLimit() == null ? 0 : mapper.getMaxLimit().longValue());
						product.setMinPeriod(mapper.getSavingOptions().stream()
							.mapToInt(SavingOption::getSaveTerm)
							.min()
							.orElse(0));
						product.setMaxPeriod(mapper.getSavingOptions().stream()
							.mapToInt(SavingOption::getSaveTerm)
							.max()
							.orElse(0));
						
						List<ProductOption> productOptions = new ArrayList<>();
						mapper.getSavingOptions().stream()
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
						
						product.getProductOptions().clear();
						product.getProductOptions().addAll(productOptions);
						return productRepository.save(product);
					}).orElseGet(() -> {
						Product product = Product.from(mapper);
						financeCompanyRepository.findByCode(product.getCompanyNumber()).ifPresent(consumer -> {
							product.setFinanceCompany(consumer);
						});
						return productRepository.save(product);
					});
			}).collect(Collectors.toList());
	}
	
	public List<Product> mergeToDeposits(List<Deposit> deposits) {
		return deposits.stream().map(mapper -> {
			return productRepository.findByCodeAndCompanyNumber(mapper.getFinanceProductCode(), mapper.getFinanceCompanyNumber())
					.map(product -> {
						financeCompanyRepository.findByCode(product.getCompanyNumber()).ifPresent(consumer -> {
							product.setFinanceCompany(consumer);
						});
						
						product.setDisclosureMonth(mapper.getDisclosureMonth());
						product.setJoinWay(mapper.getJoinWay());
						product.setMaturityInterest(mapper.getMaturityInterest());
						product.setSpecialCondition(mapper.getSpecialCondition());
						product.setJoinDeny(mapper.getJoinDeny());
						product.setJoinMember(mapper.getJoinMember());
						product.setEtcNote(mapper.getEtcNote());
						product.setMaxBalance(mapper.getMaxLimit() == null ? 0 : mapper.getMaxLimit().longValue());
						product.setMinPeriod(mapper.getDepositOptions().stream()
							.mapToInt(DepositOption::getSaveTerm)
							.min()
							.orElse(0));
						product.setMaxPeriod(mapper.getDepositOptions().stream()
							.mapToInt(DepositOption::getSaveTerm)
							.max()
							.orElse(0));
						
						List<ProductOption> productOptions = mapper.getDepositOptions().stream()
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
						product.getProductOptions().clear();
						product.getProductOptions().addAll(productOptions);
						return productRepository.save(product);
					}).orElseGet(() -> {
						Product product = Product.from(mapper);
						financeCompanyRepository.findByCode(product.getCompanyNumber()).ifPresent(consumer -> {
							product.setFinanceCompany(consumer);
						});
						return productRepository.save(product);
					});
		}).collect(Collectors.toList());
	}
	
}
