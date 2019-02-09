package com.poseidon.dolphin.simulator.company.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.dolphin.api.fss.company.Company;
import com.poseidon.dolphin.simulator.TestState;
import com.poseidon.dolphin.simulator.company.FinanceCompany;
import com.poseidon.dolphin.simulator.company.FinanceCompanyCommand;
import com.poseidon.dolphin.simulator.company.FinanceCompanyOption;
import com.poseidon.dolphin.simulator.company.repository.FinanceCompanyRepository;

@Service
public class FinanceCompanyService {
	private final FinanceCompanyRepository financeCompanyRepository;
	
	@Autowired
	public FinanceCompanyService(FinanceCompanyRepository financeCompanyRepository) {
		this.financeCompanyRepository = financeCompanyRepository;
	}

	public List<FinanceCompany> mergeTo(List<Company> companies) {
		return companies.stream().map(mapper -> {
			return financeCompanyRepository.findByCode(mapper.getFinanceCompanyNumber()).map(financeCompany -> {
				financeCompany.setDisclosureMonth(mapper.getDisclosureMonth());
				financeCompany.setFinanceGroup(mapper.getFinanceGroup());
				financeCompany.setName(mapper.getKoreanFinanceCompanyName());
				financeCompany.setDisclosureChargeman(mapper.getDisclosureChargeMan());
				financeCompany.setTel(mapper.getCallCenterTel());
				financeCompany.setWebSite(mapper.getHomepageUrl());
				financeCompany.getOptions().clear();
				financeCompany.getOptions().addAll(mapper.getCompanyOptions().stream().map(companyOption -> {
					return new FinanceCompanyOption(companyOption.getArea(), companyOption.getExistsYn().equalsIgnoreCase("Y"));
				}).collect(Collectors.toList()));
				financeCompany.setTestState(TestState.DONE);
				return financeCompanyRepository.save(financeCompany);
			}).orElseGet(() -> {
				return financeCompanyRepository.save(FinanceCompany.from(mapper));
			});
		}).collect(Collectors.toList());
	}
	
	public FinanceCompany save(FinanceCompanyCommand financeCompanyCommand) {
		return financeCompanyRepository.findById(financeCompanyCommand.getId()).map(mapper -> {
			mapper.setCode(financeCompanyCommand.getCode());
			mapper.setName(financeCompanyCommand.getName());
			mapper.setFinanceGroup(financeCompanyCommand.getFinanceGroup());
			mapper.setDisclosureMonth(financeCompanyCommand.getDisclosureMonth());
			mapper.setDisclosureChargeman(financeCompanyCommand.getDisclosureChargeman());
			mapper.setTel(financeCompanyCommand.getTel());
			mapper.setWebSite(financeCompanyCommand.getWebSite());
			mapper.setTestState(financeCompanyCommand.getTestState());
			return financeCompanyRepository.save(mapper);
		}).orElseThrow(NullPointerException::new);
	}

}
