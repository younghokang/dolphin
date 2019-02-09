package com.poseidon.dolphin.simulator.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.common.InterestRateType;
import com.poseidon.dolphin.api.fss.common.JoinDeny;
import com.poseidon.dolphin.api.fss.common.ReserveType;
import com.poseidon.dolphin.api.fss.deposit.Deposit;
import com.poseidon.dolphin.api.fss.deposit.DepositOption;
import com.poseidon.dolphin.api.fss.saving.Saving;
import com.poseidon.dolphin.api.fss.saving.SavingOption;

public class ProductTests {
	private Saving saving = makeDummySaving();
	private Deposit deposit = makeDummyDeposit();
	
	private Saving makeDummySaving() {
		Saving saving = new Saving();
		saving.setId(482l);
		saving.setFinanceGroup(FinanceGroup.BANK);
		saving.setDisclosureMonth("201901");
		saving.setFinanceCompanyNumber("0010002");
		saving.setFinanceProductCode("00266451");
		saving.setKoreanFinanceCompanyName("한국스탠다드차타드은행");
		saving.setFinanceProductName("퍼스트가계적금");
		saving.setJoinWay("영업점,인터넷,스마트폰");
		saving.setMaturityInterest("만기 후 1개월: 약정이율의 50%\n" + 
				"만기 후 1개월 초과 1년 이내: 약정이율의 30%\n" + 
				"만기 후 1년 초과: 약정이율의 10%");
		saving.setSpecialCondition("없음");
		saving.setJoinDeny(JoinDeny.NONE);
		saving.setJoinMember("제한없음");
		saving.setEtcNote("해당없음");
		saving.setMaxLimit(10000000l);
		saving.setDisclosureStartDay(LocalDate.parse("2019-01-21"));
		saving.setDisclosureEndDay(null);
		saving.setFinanceCompanySubmitDay(LocalDateTime.parse("2019-01-21T17:46"));
		
		List<SavingOption> options = saving.getSavingOptions();
		SavingOption savingOption = new SavingOption();
		savingOption.setDisclosureMonth("201901");
		savingOption.setFinanceCompanyNumber("0010002");
		savingOption.setFinanceProductCode("00266451");
		savingOption.setInterestRateType(InterestRateType.SIMPLE);
		savingOption.setInterestRateTypeName("단리");
		savingOption.setReserveType(ReserveType.FIXED);
		savingOption.setReserveTypeName("정액적립식");
		savingOption.setSaveTerm(6);
		savingOption.setInterestRate(1.6);
		savingOption.setPrimeRate(1.6);
		options.add(savingOption);
		
		savingOption = new SavingOption();
		savingOption.setDisclosureMonth("201901");
		savingOption.setFinanceCompanyNumber("0010002");
		savingOption.setFinanceProductCode("00266451");
		savingOption.setInterestRateType(InterestRateType.SIMPLE);
		savingOption.setInterestRateTypeName("단리");
		savingOption.setReserveType(ReserveType.FIXED);
		savingOption.setReserveTypeName("정액적립식");
		savingOption.setSaveTerm(12);
		savingOption.setInterestRate(1.9);
		savingOption.setPrimeRate(1.9);
		options.add(savingOption);
		
		savingOption = new SavingOption();
		savingOption.setDisclosureMonth("201901");
		savingOption.setFinanceCompanyNumber("0010002");
		savingOption.setFinanceProductCode("00266451");
		savingOption.setInterestRateType(InterestRateType.SIMPLE);
		savingOption.setInterestRateTypeName("단리");
		savingOption.setReserveType(ReserveType.FIXED);
		savingOption.setReserveTypeName("정액적립식");
		savingOption.setSaveTerm(24);
		savingOption.setInterestRate(2.0);
		savingOption.setPrimeRate(2.0);
		options.add(savingOption);
		
		savingOption = new SavingOption();
		savingOption.setDisclosureMonth("201901");
		savingOption.setFinanceCompanyNumber("0010002");
		savingOption.setFinanceProductCode("00266451");
		savingOption.setInterestRateType(InterestRateType.SIMPLE);
		savingOption.setInterestRateTypeName("단리");
		savingOption.setReserveType(ReserveType.FIXED);
		savingOption.setReserveTypeName("정액적립식");
		savingOption.setSaveTerm(36);
		savingOption.setInterestRate(2.1);
		savingOption.setPrimeRate(2.1);
		options.add(savingOption);
		
		savingOption = new SavingOption();
		savingOption.setDisclosureMonth("201901");
		savingOption.setFinanceCompanyNumber("0010002");
		savingOption.setFinanceProductCode("00266451");
		savingOption.setInterestRateType(InterestRateType.SIMPLE);
		savingOption.setInterestRateTypeName("단리");
		savingOption.setReserveType(ReserveType.FREE);
		savingOption.setReserveTypeName("자유적립식");
		savingOption.setSaveTerm(6);
		savingOption.setInterestRate(1.5);
		savingOption.setPrimeRate(1.5);
		options.add(savingOption);
		
		savingOption = new SavingOption();
		savingOption.setDisclosureMonth("201901");
		savingOption.setFinanceCompanyNumber("0010002");
		savingOption.setFinanceProductCode("00266451");
		savingOption.setInterestRateType(InterestRateType.SIMPLE);
		savingOption.setInterestRateTypeName("단리");
		savingOption.setReserveType(ReserveType.FREE);
		savingOption.setReserveTypeName("자유적립식");
		savingOption.setSaveTerm(12);
		savingOption.setInterestRate(1.7);
		savingOption.setPrimeRate(1.7);
		options.add(savingOption);
		
		savingOption = new SavingOption();
		savingOption.setDisclosureMonth("201901");
		savingOption.setFinanceCompanyNumber("0010002");
		savingOption.setFinanceProductCode("00266451");
		savingOption.setInterestRateType(InterestRateType.SIMPLE);
		savingOption.setInterestRateTypeName("단리");
		savingOption.setReserveType(ReserveType.FREE);
		savingOption.setReserveTypeName("자유적립식");
		savingOption.setSaveTerm(24);
		savingOption.setInterestRate(1.8);
		savingOption.setPrimeRate(1.8);
		options.add(savingOption);
		
		savingOption = new SavingOption();
		savingOption.setDisclosureMonth("201901");
		savingOption.setFinanceCompanyNumber("0010002");
		savingOption.setFinanceProductCode("00266451");
		savingOption.setInterestRateType(InterestRateType.SIMPLE);
		savingOption.setInterestRateTypeName("단리");
		savingOption.setReserveType(ReserveType.FREE);
		savingOption.setReserveTypeName("자유적립식");
		savingOption.setSaveTerm(36);
		savingOption.setInterestRate(1.9);
		savingOption.setPrimeRate(1.9);
		options.add(savingOption);
		return saving;
	}
	
	private Deposit makeDummyDeposit() {
		Deposit deposit = new Deposit();
		deposit.setId(294l);
		deposit.setFinanceGroup(FinanceGroup.BANK);
		deposit.setDisclosureMonth("201901");
		deposit.setFinanceCompanyNumber("0010345");
		deposit.setFinanceProductCode("HK00001");
		deposit.setKoreanFinanceCompanyName("애큐온저축은행");
		deposit.setFinanceProductName("정기예금");
		deposit.setJoinWay("영업점,인터넷,스마트폰,전화(텔레뱅킹)");
		deposit.setMaturityInterest("- 보통예금이율 (연 0.1%)");
		deposit.setSpecialCondition("- 인터넷뱅킹 가입: 연 0.05%\n" + 
				" - 모바일뱅킹 가입: 연 0.10%");
		deposit.setJoinDeny(JoinDeny.NONE);
		deposit.setJoinMember("제한없음");
		deposit.setEtcNote("- 계약금액 1백만원 이상");
		deposit.setMaxLimit(0l);
		deposit.setDisclosureStartDay(LocalDate.parse("2019-01-21"));
		deposit.setFinanceCompanySubmitDay(LocalDateTime.parse("2019-01-18T16:00"));
		
		List<DepositOption> options = deposit.getDepositOptions();
		DepositOption option = new DepositOption();
		option.setDisclosureMonth("201901");
		option.setFinanceCompanyNumber("0010345");
		option.setFinanceProductCode("HK00001");
		option.setInterestRateType(InterestRateType.SIMPLE);
		option.setInterestRateTypeName("단리");
		option.setSaveTerm(6);
		option.setInterestRate(1.8);
		option.setPrimeRate(1.9);
		options.add(option);
		
		option = new DepositOption();
		option.setDisclosureMonth("201901");
		option.setFinanceCompanyNumber("0010345");
		option.setFinanceProductCode("HK00001");
		option.setInterestRateType(InterestRateType.SIMPLE);
		option.setInterestRateTypeName("단리");
		option.setSaveTerm(12);
		option.setInterestRate(2.5);
		option.setPrimeRate(2.6);
		options.add(option);
		
		option = new DepositOption();
		option.setDisclosureMonth("201901");
		option.setFinanceCompanyNumber("0010345");
		option.setFinanceProductCode("HK00001");
		option.setInterestRateType(InterestRateType.SIMPLE);
		option.setInterestRateTypeName("단리");
		option.setSaveTerm(24);
		option.setInterestRate(2.8);
		option.setPrimeRate(2.9);
		options.add(option);
		
		option = new DepositOption();
		option.setDisclosureMonth("201901");
		option.setFinanceCompanyNumber("0010345");
		option.setFinanceProductCode("HK00001");
		option.setInterestRateType(InterestRateType.SIMPLE);
		option.setInterestRateTypeName("단리");
		option.setSaveTerm(36);
		option.setInterestRate(2.9);
		option.setPrimeRate(3.0);
		options.add(option);
		
		option = new DepositOption();
		option.setDisclosureMonth("201901");
		option.setFinanceCompanyNumber("0010345");
		option.setFinanceProductCode("HK00001");
		option.setInterestRateType(InterestRateType.COMPOUND);
		option.setInterestRateTypeName("복리");
		option.setSaveTerm(6);
		option.setInterestRate(1.8);
		option.setPrimeRate(1.9);
		options.add(option);
		
		option = new DepositOption();
		option.setDisclosureMonth("201901");
		option.setFinanceCompanyNumber("0010345");
		option.setFinanceProductCode("HK00001");
		option.setInterestRateType(InterestRateType.COMPOUND);
		option.setInterestRateTypeName("복리");
		option.setSaveTerm(12);
		option.setInterestRate(2.5);
		option.setPrimeRate(2.6);
		options.add(option);
		
		option = new DepositOption();
		option.setDisclosureMonth("201901");
		option.setFinanceCompanyNumber("0010345");
		option.setFinanceProductCode("HK00001");
		option.setInterestRateType(InterestRateType.COMPOUND);
		option.setInterestRateTypeName("복리");
		option.setSaveTerm(24);
		option.setInterestRate(2.8);
		option.setPrimeRate(2.9);
		options.add(option);
		
		option = new DepositOption();
		option.setDisclosureMonth("201901");
		option.setFinanceCompanyNumber("0010345");
		option.setFinanceProductCode("HK00001");
		option.setInterestRateType(InterestRateType.COMPOUND);
		option.setInterestRateTypeName("복리");
		option.setSaveTerm(36);
		option.setInterestRate(2.9);
		option.setPrimeRate(3.0);
		options.add(option);
		return deposit;
	}
	
	@Test
	public void makeGroupingSavingOptions() {
		List<SavingOption> savingOptions = saving.getSavingOptions();
		savingOptions.stream().forEach(option -> {
			System.out.printf("저축금리유형: %s, 저축금리유형명: %s, 적립유형: %s, 적립유형명: %s, ", option.getInterestRateType(), option.getInterestRateTypeName(), option.getReserveType(), option.getReserveTypeName());
			System.out.printf("저축기간[개월]: %d, 저축금리[소수점 2자리]: %.2f, 우대금리[소수점 2자리]: %.2f\n", option.getSaveTerm(), option.getInterestRate(), option.getPrimeRate());
		});
		System.out.println();
		
		validSavingOptions(savingOptions);
		
		List<ProductOption> productOptions = new ArrayList<>();
		
		Map<InterestRateType, Map<ReserveType, List<SavingOption>>> groupingSavingOptions = savingOptions.stream()
				.collect(Collectors.groupingBy(SavingOption::getInterestRateType, Collectors.groupingBy(SavingOption::getReserveType)));
		groupingSavingOptions.forEach((interestRateType, map)-> {
			System.out.printf("저축 금리 유형: %s\n", interestRateType);
			map.forEach((reserveType, options) -> {
				System.out.printf("\t적립 유형: %s\n", reserveType);
				options.stream().forEach(item -> {
					System.out.printf("\t\t[SavingOption: 저축금리유형: %s, 적립유형: %s, 기간: %d, 금리: %.2f, 우대금리: %.2f]\n", item.getInterestRateType(), item.getReserveType(), item.getSaveTerm(), item.getInterestRate(), item.getPrimeRate());
				});
			});
		});
		System.out.println();
		
		assertThat(groupingSavingOptions.keySet()).containsOnly(InterestRateType.SIMPLE);
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).keySet().size()).isEqualTo(2);
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).keySet()).contains(ReserveType.values());
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FREE).size()).isEqualTo(4);
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FREE).get(0).getSaveTerm()).isEqualTo(6);
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FREE).get(0).getInterestRate()).isCloseTo(1.50, within(0.001));
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FREE).get(0).getPrimeRate()).isCloseTo(1.50, within(0.001));
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FREE).get(1).getSaveTerm()).isEqualTo(12);
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FREE).get(1).getInterestRate()).isCloseTo(1.70, within(0.001));
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FREE).get(1).getPrimeRate()).isCloseTo(1.70, within(0.001));
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FREE).get(2).getSaveTerm()).isEqualTo(24);
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FREE).get(2).getInterestRate()).isCloseTo(1.80, within(0.001));
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FREE).get(2).getPrimeRate()).isCloseTo(1.80, within(0.001));
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FREE).get(3).getSaveTerm()).isEqualTo(36);
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FREE).get(3).getInterestRate()).isCloseTo(1.90, within(0.001));
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FREE).get(3).getPrimeRate()).isCloseTo(1.90, within(0.001));
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FIXED).size()).isEqualTo(4);
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FIXED).get(0).getSaveTerm()).isEqualTo(6);
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FIXED).get(0).getInterestRate()).isCloseTo(1.60, within(0.001));
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FIXED).get(0).getPrimeRate()).isCloseTo(1.60, within(0.001));
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FIXED).get(1).getSaveTerm()).isEqualTo(12);
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FIXED).get(1).getInterestRate()).isCloseTo(1.90, within(0.001));
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FIXED).get(1).getPrimeRate()).isCloseTo(1.90, within(0.001));
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FIXED).get(2).getSaveTerm()).isEqualTo(24);
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FIXED).get(2).getInterestRate()).isCloseTo(2.00, within(0.001));
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FIXED).get(2).getPrimeRate()).isCloseTo(2.00, within(0.001));
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FIXED).get(3).getSaveTerm()).isEqualTo(36);
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FIXED).get(3).getInterestRate()).isCloseTo(2.10, within(0.001));
		assertThat(groupingSavingOptions.get(InterestRateType.SIMPLE).get(ReserveType.FIXED).get(3).getPrimeRate()).isCloseTo(2.10, within(0.001));
		
		groupingSavingOptions.entrySet().stream()
			.sorted()
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
		
		productOptions.stream().forEach(item -> {
			System.out.printf("[ProductOption: 저축금리유형: %s, 적립유형: %s]\n", item.getInterestRateType(), item.getReserveType());
			item.getInterestRates().stream().forEach(interestRate -> {
				System.out.printf("\t[InterestRate: 기간: %d, 금리: %.3f, 우대금리: %.3f]\n", interestRate.getContractPeriod(), interestRate.getRate(), interestRate.getPrimeRate());
			});
		});
		System.out.println();
		
		validProductOptionsFromSaving(productOptions);
		
	}

	private void validSavingOptions(List<SavingOption> savingOptions) {
		assertThat(savingOptions.get(0).getInterestRateType()).isEqualTo(InterestRateType.SIMPLE);
		assertThat(savingOptions.get(0).getReserveType()).isEqualTo(ReserveType.FIXED);
		assertThat(savingOptions.get(0).getSaveTerm()).isEqualTo(6);
		assertThat(savingOptions.get(0).getInterestRate()).isCloseTo(1.60, within(0.001));
		assertThat(savingOptions.get(0).getPrimeRate()).isCloseTo(1.60, within(0.001));
		
		assertThat(savingOptions.get(1).getInterestRateType()).isEqualTo(InterestRateType.SIMPLE);
		assertThat(savingOptions.get(1).getReserveType()).isEqualTo(ReserveType.FIXED);
		assertThat(savingOptions.get(1).getSaveTerm()).isEqualTo(12);
		assertThat(savingOptions.get(1).getInterestRate()).isCloseTo(1.90, within(0.001));
		assertThat(savingOptions.get(1).getPrimeRate()).isCloseTo(1.90, within(0.001));
		
		assertThat(savingOptions.get(2).getInterestRateType()).isEqualTo(InterestRateType.SIMPLE);
		assertThat(savingOptions.get(2).getReserveType()).isEqualTo(ReserveType.FIXED);
		assertThat(savingOptions.get(2).getSaveTerm()).isEqualTo(24);
		assertThat(savingOptions.get(2).getInterestRate()).isCloseTo(2.00, within(0.001));
		assertThat(savingOptions.get(2).getPrimeRate()).isCloseTo(2.00, within(0.001));
		
		assertThat(savingOptions.get(3).getInterestRateType()).isEqualTo(InterestRateType.SIMPLE);
		assertThat(savingOptions.get(3).getReserveType()).isEqualTo(ReserveType.FIXED);
		assertThat(savingOptions.get(3).getSaveTerm()).isEqualTo(36);
		assertThat(savingOptions.get(3).getInterestRate()).isCloseTo(2.10, within(0.001));
		assertThat(savingOptions.get(3).getPrimeRate()).isCloseTo(2.10, within(0.001));
		
		assertThat(savingOptions.get(4).getInterestRateType()).isEqualTo(InterestRateType.SIMPLE);
		assertThat(savingOptions.get(4).getReserveType()).isEqualTo(ReserveType.FREE);
		assertThat(savingOptions.get(4).getSaveTerm()).isEqualTo(6);
		assertThat(savingOptions.get(4).getInterestRate()).isCloseTo(1.50, within(0.001));
		assertThat(savingOptions.get(4).getPrimeRate()).isCloseTo(1.50, within(0.001));
		
		assertThat(savingOptions.get(5).getInterestRateType()).isEqualTo(InterestRateType.SIMPLE);
		assertThat(savingOptions.get(5).getReserveType()).isEqualTo(ReserveType.FREE);
		assertThat(savingOptions.get(5).getSaveTerm()).isEqualTo(12);
		assertThat(savingOptions.get(5).getInterestRate()).isCloseTo(1.70, within(0.001));
		assertThat(savingOptions.get(5).getPrimeRate()).isCloseTo(1.70, within(0.001));
		
		assertThat(savingOptions.get(6).getInterestRateType()).isEqualTo(InterestRateType.SIMPLE);
		assertThat(savingOptions.get(6).getReserveType()).isEqualTo(ReserveType.FREE);
		assertThat(savingOptions.get(6).getSaveTerm()).isEqualTo(24);
		assertThat(savingOptions.get(6).getInterestRate()).isCloseTo(1.80, within(0.001));
		assertThat(savingOptions.get(6).getPrimeRate()).isCloseTo(1.80, within(0.001));
		
		assertThat(savingOptions.get(7).getInterestRateType()).isEqualTo(InterestRateType.SIMPLE);
		assertThat(savingOptions.get(7).getReserveType()).isEqualTo(ReserveType.FREE);
		assertThat(savingOptions.get(7).getSaveTerm()).isEqualTo(36);
		assertThat(savingOptions.get(7).getInterestRate()).isCloseTo(1.90, within(0.001));
		assertThat(savingOptions.get(7).getPrimeRate()).isCloseTo(1.90, within(0.001));
	}
	
	private void validProductOptionsFromSaving(List<ProductOption> productOptions) {
		assertThat(productOptions).allMatch(p -> p.getInterestRateType().equals(InterestRateType.SIMPLE));
		assertThat(productOptions.get(0).getReserveType()).isEqualTo(ReserveType.FREE);
		assertThat(productOptions.get(0).getInterestRates().get(0).getContractPeriod()).isEqualTo(6);
		assertThat(productOptions.get(0).getInterestRates().get(0).getRate()).isCloseTo(0.015, within(0.0001));
		assertThat(productOptions.get(0).getInterestRates().get(0).getPrimeRate()).isCloseTo(0.015, within(0.0001));
		assertThat(productOptions.get(0).getInterestRates().get(1).getContractPeriod()).isEqualTo(12);
		assertThat(productOptions.get(0).getInterestRates().get(1).getRate()).isCloseTo(0.017, within(0.0001));
		assertThat(productOptions.get(0).getInterestRates().get(1).getPrimeRate()).isCloseTo(0.017, within(0.0001));
		assertThat(productOptions.get(0).getInterestRates().get(2).getContractPeriod()).isEqualTo(24);
		assertThat(productOptions.get(0).getInterestRates().get(2).getRate()).isCloseTo(0.018, within(0.0001));
		assertThat(productOptions.get(0).getInterestRates().get(2).getPrimeRate()).isCloseTo(0.018, within(0.0001));
		assertThat(productOptions.get(0).getInterestRates().get(3).getContractPeriod()).isEqualTo(36);
		assertThat(productOptions.get(0).getInterestRates().get(3).getRate()).isCloseTo(0.019, within(0.0001));
		assertThat(productOptions.get(0).getInterestRates().get(3).getPrimeRate()).isCloseTo(0.019, within(0.0001));
		
		assertThat(productOptions.get(1).getReserveType()).isEqualTo(ReserveType.FIXED);
		assertThat(productOptions.get(1).getInterestRates().get(0).getContractPeriod()).isEqualTo(6);
		assertThat(productOptions.get(1).getInterestRates().get(0).getRate()).isCloseTo(0.016, within(0.0001));
		assertThat(productOptions.get(1).getInterestRates().get(0).getPrimeRate()).isCloseTo(0.016, within(0.0001));
		assertThat(productOptions.get(1).getInterestRates().get(1).getContractPeriod()).isEqualTo(12);
		assertThat(productOptions.get(1).getInterestRates().get(1).getRate()).isCloseTo(0.019, within(0.0001));
		assertThat(productOptions.get(1).getInterestRates().get(1).getPrimeRate()).isCloseTo(0.019, within(0.0001));
		assertThat(productOptions.get(1).getInterestRates().get(2).getContractPeriod()).isEqualTo(24);
		assertThat(productOptions.get(1).getInterestRates().get(2).getRate()).isCloseTo(0.020, within(0.0001));
		assertThat(productOptions.get(1).getInterestRates().get(2).getPrimeRate()).isCloseTo(0.020, within(0.0001));
		assertThat(productOptions.get(1).getInterestRates().get(3).getContractPeriod()).isEqualTo(36);
		assertThat(productOptions.get(1).getInterestRates().get(3).getRate()).isCloseTo(0.021, within(0.0001));
		assertThat(productOptions.get(1).getInterestRates().get(3).getPrimeRate()).isCloseTo(0.021, within(0.0001));
	}
	
	@Test
	public void productFromSaving() {
		List<SavingOption> savingOptions = saving.getSavingOptions();
		validSavingOptions(savingOptions);
		
		Product product = Product.from(saving);
		assertThat(product).isNotNull();
		assertThat(product.getProductType()).isEqualTo(ProductType.INSTALLMENT_SAVING);
		assertThat(product.getDisclosureMonth()).isEqualTo(saving.getDisclosureMonth());
		assertThat(product.getCompanyNumber()).isEqualTo(saving.getFinanceCompanyNumber());
		assertThat(product.getCode()).isEqualTo(saving.getFinanceProductCode());
		assertThat(product.getCompanyName()).isEqualTo(saving.getKoreanFinanceCompanyName());
		assertThat(product.getName()).isEqualTo(saving.getFinanceProductName());
		assertThat(product.getJoinWay()).isEqualTo(saving.getJoinWay());
		assertThat(product.getMaturityInterest()).isEqualTo(saving.getMaturityInterest());
		assertThat(product.getSpecialCondition()).isEqualTo(saving.getSpecialCondition());
		assertThat(product.getJoinDeny()).isEqualTo(saving.getJoinDeny());
		assertThat(product.getJoinMember()).isEqualTo(saving.getJoinMember());
		assertThat(product.getEtcNote()).isEqualTo(saving.getEtcNote());
		assertThat(product.getMaxBalance()).isEqualTo(saving.getMaxLimit());
		
		List<ProductOption> productOptions = product.getProductOptions();
		validProductOptionsFromSaving(productOptions);
	}
	
	@Test
	public void makeGroupingDepositOptions() {
		List<DepositOption> depositOptions = deposit.getDepositOptions();
		depositOptions.stream().forEach(option -> {
			System.out.printf("저축 금리 유형: %s, 저축 금리 유형명: %s, ", option.getInterestRateType(), option.getInterestRateTypeName());
			System.out.printf("저축 기간[개월]: %d, 저축 금리[소수점 2자리]: %.2f, 우대 금리[소수점 2자리]: %.2f\n", option.getSaveTerm(), option.getInterestRate(), option.getPrimeRate());
		});
		System.out.println();
		
		validDepositOptions(depositOptions);
		
		List<ProductOption> productOptions = new ArrayList<>();
		
		Map<InterestRateType, List<DepositOption>> groupingDepositOptions = depositOptions.stream()
				.collect(Collectors.groupingBy(DepositOption::getInterestRateType));
		groupingDepositOptions.forEach((interestRateType, options)-> {
			System.out.printf("저축 금리 유형: %s\n", interestRateType);
			options.stream().forEach(item -> {
				System.out.printf("\t[DepositOption: 저축금리유형: %s, 기간: %d, 금리: %.2f, 우대금리: %.2f]\n", item.getInterestRateType(), item.getSaveTerm(), item.getInterestRate(), item.getPrimeRate());
			});
		});
		System.out.println();
		
		assertThat(groupingDepositOptions.keySet()).contains(InterestRateType.SIMPLE, InterestRateType.COMPOUND);
		assertThat(groupingDepositOptions.get(InterestRateType.SIMPLE).size()).isEqualTo(4);
		assertThat(groupingDepositOptions.get(InterestRateType.SIMPLE).get(0).getSaveTerm()).isEqualTo(6);
		assertThat(groupingDepositOptions.get(InterestRateType.SIMPLE).get(0).getInterestRate()).isEqualTo(1.80);
		assertThat(groupingDepositOptions.get(InterestRateType.SIMPLE).get(0).getPrimeRate()).isEqualTo(1.90);
		assertThat(groupingDepositOptions.get(InterestRateType.SIMPLE).get(1).getSaveTerm()).isEqualTo(12);
		assertThat(groupingDepositOptions.get(InterestRateType.SIMPLE).get(1).getInterestRate()).isEqualTo(2.50);
		assertThat(groupingDepositOptions.get(InterestRateType.SIMPLE).get(1).getPrimeRate()).isEqualTo(2.60);
		assertThat(groupingDepositOptions.get(InterestRateType.SIMPLE).get(2).getSaveTerm()).isEqualTo(24);
		assertThat(groupingDepositOptions.get(InterestRateType.SIMPLE).get(2).getInterestRate()).isEqualTo(2.80);
		assertThat(groupingDepositOptions.get(InterestRateType.SIMPLE).get(2).getPrimeRate()).isEqualTo(2.90);
		assertThat(groupingDepositOptions.get(InterestRateType.SIMPLE).get(3).getSaveTerm()).isEqualTo(36);
		assertThat(groupingDepositOptions.get(InterestRateType.SIMPLE).get(3).getInterestRate()).isEqualTo(2.90);
		assertThat(groupingDepositOptions.get(InterestRateType.SIMPLE).get(3).getPrimeRate()).isEqualTo(3.00);
		
		assertThat(groupingDepositOptions.get(InterestRateType.COMPOUND).size()).isEqualTo(4);
		assertThat(groupingDepositOptions.get(InterestRateType.COMPOUND).get(0).getSaveTerm()).isEqualTo(6);
		assertThat(groupingDepositOptions.get(InterestRateType.COMPOUND).get(0).getInterestRate()).isEqualTo(1.80);
		assertThat(groupingDepositOptions.get(InterestRateType.COMPOUND).get(0).getPrimeRate()).isEqualTo(1.90);
		assertThat(groupingDepositOptions.get(InterestRateType.COMPOUND).get(1).getSaveTerm()).isEqualTo(12);
		assertThat(groupingDepositOptions.get(InterestRateType.COMPOUND).get(1).getInterestRate()).isEqualTo(2.50);
		assertThat(groupingDepositOptions.get(InterestRateType.COMPOUND).get(1).getPrimeRate()).isEqualTo(2.60);
		assertThat(groupingDepositOptions.get(InterestRateType.COMPOUND).get(2).getSaveTerm()).isEqualTo(24);
		assertThat(groupingDepositOptions.get(InterestRateType.COMPOUND).get(2).getInterestRate()).isEqualTo(2.80);
		assertThat(groupingDepositOptions.get(InterestRateType.COMPOUND).get(2).getPrimeRate()).isEqualTo(2.90);
		assertThat(groupingDepositOptions.get(InterestRateType.COMPOUND).get(3).getSaveTerm()).isEqualTo(36);
		assertThat(groupingDepositOptions.get(InterestRateType.COMPOUND).get(3).getInterestRate()).isEqualTo(2.90);
		assertThat(groupingDepositOptions.get(InterestRateType.COMPOUND).get(3).getPrimeRate()).isEqualTo(3.00);
		
		productOptions.addAll(groupingDepositOptions.entrySet().stream()
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
			.collect(Collectors.toList()));
		
		productOptions.stream().forEach(item -> {
				System.out.printf("[ProductOption: 저축금리유형: %s, 적립유형: %s]\n", item.getInterestRateType(), item.getReserveType());
				item.getInterestRates().stream().forEach(interestRate -> {
					System.out.printf("\t[InterestRate: 기간: %d, 금리: %.2f, 우대금리: %.2f]\n", interestRate.getContractPeriod(), interestRate.getRate(), interestRate.getPrimeRate());
				});
			});
		System.out.println();
		
		validProductOptionsFromDeposit(productOptions);
	}
	
	private void validDepositOptions(List<DepositOption> depositOptions) {
		assertThat(depositOptions.get(0).getInterestRateType()).isEqualTo(InterestRateType.SIMPLE);
		assertThat(depositOptions.get(0).getSaveTerm()).isEqualTo(6);
		assertThat(depositOptions.get(0).getInterestRate()).isEqualTo(1.80);
		assertThat(depositOptions.get(0).getPrimeRate()).isEqualTo(1.90);
		
		assertThat(depositOptions.get(1).getInterestRateType()).isEqualTo(InterestRateType.SIMPLE);
		assertThat(depositOptions.get(1).getSaveTerm()).isEqualTo(12);
		assertThat(depositOptions.get(1).getInterestRate()).isEqualTo(2.50);
		assertThat(depositOptions.get(1).getPrimeRate()).isEqualTo(2.60);
		
		assertThat(depositOptions.get(2).getInterestRateType()).isEqualTo(InterestRateType.SIMPLE);
		assertThat(depositOptions.get(2).getSaveTerm()).isEqualTo(24);
		assertThat(depositOptions.get(2).getInterestRate()).isEqualTo(2.80);
		assertThat(depositOptions.get(2).getPrimeRate()).isEqualTo(2.90);
		
		assertThat(depositOptions.get(3).getInterestRateType()).isEqualTo(InterestRateType.SIMPLE);
		assertThat(depositOptions.get(3).getSaveTerm()).isEqualTo(36);
		assertThat(depositOptions.get(3).getInterestRate()).isEqualTo(2.90);
		assertThat(depositOptions.get(3).getPrimeRate()).isEqualTo(3.00);
		
		assertThat(depositOptions.get(4).getInterestRateType()).isEqualTo(InterestRateType.COMPOUND);
		assertThat(depositOptions.get(4).getSaveTerm()).isEqualTo(6);
		assertThat(depositOptions.get(4).getInterestRate()).isEqualTo(1.80);
		assertThat(depositOptions.get(4).getPrimeRate()).isEqualTo(1.90);
		
		assertThat(depositOptions.get(5).getInterestRateType()).isEqualTo(InterestRateType.COMPOUND);
		assertThat(depositOptions.get(5).getSaveTerm()).isEqualTo(12);
		assertThat(depositOptions.get(5).getInterestRate()).isEqualTo(2.50);
		assertThat(depositOptions.get(5).getPrimeRate()).isEqualTo(2.60);
		
		assertThat(depositOptions.get(6).getInterestRateType()).isEqualTo(InterestRateType.COMPOUND);
		assertThat(depositOptions.get(6).getSaveTerm()).isEqualTo(24);
		assertThat(depositOptions.get(6).getInterestRate()).isEqualTo(2.80);
		assertThat(depositOptions.get(6).getPrimeRate()).isEqualTo(2.90);
		
		assertThat(depositOptions.get(7).getInterestRateType()).isEqualTo(InterestRateType.COMPOUND);
		assertThat(depositOptions.get(7).getSaveTerm()).isEqualTo(36);
		assertThat(depositOptions.get(7).getInterestRate()).isEqualTo(2.90);
		assertThat(depositOptions.get(7).getPrimeRate()).isEqualTo(3.00);
	}
	
	private void validProductOptionsFromDeposit(List<ProductOption> productOptions) {
		assertThat(productOptions.get(0).getInterestRateType()).isEqualTo(InterestRateType.SIMPLE);
		assertThat(productOptions.get(0).getInterestRates().get(0).getContractPeriod()).isEqualTo(6);
		assertThat(productOptions.get(0).getInterestRates().get(0).getRate()).isCloseTo(0.018, within(0.0001));
		assertThat(productOptions.get(0).getInterestRates().get(0).getPrimeRate()).isCloseTo(0.019, within(0.0001));
		assertThat(productOptions.get(0).getInterestRates().get(1).getContractPeriod()).isEqualTo(12);
		assertThat(productOptions.get(0).getInterestRates().get(1).getRate()).isCloseTo(0.025, within(0.0001));
		assertThat(productOptions.get(0).getInterestRates().get(1).getPrimeRate()).isCloseTo(0.026, within(0.0001));
		assertThat(productOptions.get(0).getInterestRates().get(2).getContractPeriod()).isEqualTo(24);
		assertThat(productOptions.get(0).getInterestRates().get(2).getRate()).isCloseTo(0.028, within(0.0001));
		assertThat(productOptions.get(0).getInterestRates().get(2).getPrimeRate()).isCloseTo(0.029, within(0.0001));
		assertThat(productOptions.get(0).getInterestRates().get(3).getContractPeriod()).isEqualTo(36);
		assertThat(productOptions.get(0).getInterestRates().get(3).getRate()).isCloseTo(0.029, within(0.0001));
		assertThat(productOptions.get(0).getInterestRates().get(3).getPrimeRate()).isCloseTo(0.03, within(0.0001));
		assertThat(productOptions.get(1).getInterestRateType()).isEqualTo(InterestRateType.COMPOUND);
		assertThat(productOptions.get(1).getInterestRates().get(0).getContractPeriod()).isEqualTo(6);
		assertThat(productOptions.get(1).getInterestRates().get(0).getRate()).isCloseTo(0.018, within(0.0001));
		assertThat(productOptions.get(1).getInterestRates().get(0).getPrimeRate()).isCloseTo(0.019, within(0.0001));
		assertThat(productOptions.get(1).getInterestRates().get(1).getContractPeriod()).isEqualTo(12);
		assertThat(productOptions.get(1).getInterestRates().get(1).getRate()).isCloseTo(0.025, within(0.0001));
		assertThat(productOptions.get(1).getInterestRates().get(1).getPrimeRate()).isCloseTo(0.026, within(0.0001));
		assertThat(productOptions.get(1).getInterestRates().get(2).getContractPeriod()).isEqualTo(24);
		assertThat(productOptions.get(1).getInterestRates().get(2).getRate()).isCloseTo(0.028, within(0.0001));
		assertThat(productOptions.get(1).getInterestRates().get(2).getPrimeRate()).isCloseTo(0.029, within(0.0001));
		assertThat(productOptions.get(1).getInterestRates().get(3).getContractPeriod()).isEqualTo(36);
		assertThat(productOptions.get(1).getInterestRates().get(3).getRate()).isCloseTo(0.029, within(0.0001));
		assertThat(productOptions.get(1).getInterestRates().get(3).getPrimeRate()).isCloseTo(0.03, within(0.0001));
	}
	
	@Test
	public void productFromDeposit() {
		List<DepositOption> depositOptions = deposit.getDepositOptions();
		validDepositOptions(depositOptions);
		
		Product product = Product.from(deposit);
		assertThat(product).isNotNull();
		assertThat(product.getProductType()).isEqualTo(ProductType.FIXED_DEPOSIT);
		assertThat(product.getDisclosureMonth()).isEqualTo(deposit.getDisclosureMonth());
		assertThat(product.getCompanyNumber()).isEqualTo(deposit.getFinanceCompanyNumber());
		assertThat(product.getCode()).isEqualTo(deposit.getFinanceProductCode());
		assertThat(product.getCompanyName()).isEqualTo(deposit.getKoreanFinanceCompanyName());
		assertThat(product.getName()).isEqualTo(deposit.getFinanceProductName());
		assertThat(product.getJoinWay()).isEqualTo(deposit.getJoinWay());
		assertThat(product.getMaturityInterest()).isEqualTo(deposit.getMaturityInterest());
		assertThat(product.getSpecialCondition()).isEqualTo(deposit.getSpecialCondition());
		assertThat(product.getJoinDeny()).isEqualTo(deposit.getJoinDeny());
		assertThat(product.getJoinMember()).isEqualTo(deposit.getJoinMember());
		assertThat(product.getEtcNote()).isEqualTo(deposit.getEtcNote());
		assertThat(product.getMaxBalance()).isEqualTo(deposit.getMaxLimit());
		
		List<ProductOption> productOptions = product.getProductOptions();
		validProductOptionsFromDeposit(productOptions);
	}
	
}
