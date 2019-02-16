package com.poseidon.dolphin.api.fss.company.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.poseidon.dolphin.api.fss.company.Company;
import com.poseidon.dolphin.api.fss.company.repository.CompanyRepository;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {
	private final CompanyRepository companyRepository;

	@Autowired
	public CompanyServiceImpl(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

	@Override
	public List<Company> findAllLatestDisclosureMonth() {
		String disclosureMonth = companyRepository.getMaxDisclosureMonth();
		if(!StringUtils.isEmpty(disclosureMonth)) {
			return companyRepository.findAllByDisclosureMonth(disclosureMonth);
		}
		return Collections.emptyList();
	}
	
	@Override
	public Optional<Company> findByLatestDisclosureMonthAndFinanceCompanyNumber(String financeCompanyNumber) {
		String disclosureMonth = companyRepository.getMaxDisclosureMonth();
		if(!StringUtils.isEmpty(disclosureMonth)) {
			return companyRepository.findByFinanceCompanyNumberAndDisclosureMonth(financeCompanyNumber, disclosureMonth);
		}
		return Optional.empty();
	}

	@Override
	public Optional<Company> findByFinanceCompanyNumberAndDisclosureMonth(String financeCompanyNumber,
			String disclosureMonth) {
		return companyRepository.findByFinanceCompanyNumberAndDisclosureMonth(financeCompanyNumber, disclosureMonth);
	}

	@Override
	public Company save(Company company) {
		return companyRepository.save(company);
	}

}
