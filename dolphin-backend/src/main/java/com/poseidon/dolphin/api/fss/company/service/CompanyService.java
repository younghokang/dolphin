package com.poseidon.dolphin.api.fss.company.service;

import java.util.List;
import java.util.Optional;

import com.poseidon.dolphin.api.fss.company.Company;

public interface CompanyService {
	List<Company> findAllLatestDisclosureMonth();
	Optional<Company> findByLatestDisclosureMonthAndFinanceCompanyNumber(String financeCompanyNumber);
	Optional<Company> findByFinanceCompanyNumberAndDisclosureMonth(String financeCompanyNumber, String disclosureMonth);
	Company save(Company company);
}
