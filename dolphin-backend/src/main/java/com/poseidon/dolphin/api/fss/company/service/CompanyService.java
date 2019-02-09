package com.poseidon.dolphin.api.fss.company.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.poseidon.dolphin.api.fss.company.Company;
import com.poseidon.dolphin.api.fss.company.repository.CompanyRepository;

@Service
public class CompanyService {
	private final CompanyRepository companyRepository;

	@Autowired
	public CompanyService(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

	public List<Company> findAllLatestDisclosureMonth() {
		String disclosureMonth = companyRepository.getMaxDisclosureMonth();
		if(!StringUtils.isEmpty(disclosureMonth)) {
			return companyRepository.findAllByDisclosureMonth(disclosureMonth);
		}
		return Collections.emptyList();
	}
	
	public Optional<Company> findByLatestDisclosureMonthAndFinanceCompanyNumber(String financeCompanyNumber) {
		String disclosureMonth = companyRepository.getMaxDisclosureMonth();
		if(!StringUtils.isEmpty(disclosureMonth)) {
			return companyRepository.findByFinanceCompanyNumberAndDisclosureMonth(financeCompanyNumber, disclosureMonth);
		}
		return Optional.empty();
	}

}
