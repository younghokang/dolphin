package com.poseidon.dolphin.api.fss.collector;

import java.util.stream.Collectors;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.company.Company;
import com.poseidon.dolphin.api.fss.company.CompanyOption;
import com.poseidon.dolphin.api.fss.company.json.FSSCompanyResult;
import com.poseidon.dolphin.api.fss.company.repository.CompanyRepository;
import com.poseidon.dolphin.api.fss.connector.Connector;
import com.poseidon.dolphin.api.fss.result.repository.ResultRepository;

public class CompanyCollector extends AbstractCollector<FSSCompanyResult> {
	private final CompanyRepository companyRepository;
	
	public CompanyCollector(ResultRepository resultRepository, Connector<?> connector, String apiKey, CompanyRepository companyRepository) {
		super(resultRepository, connector, apiKey);
		this.companyRepository = companyRepository;
	}

	@Override
	protected void store(FSSCompanyResult result, FinanceGroup financeGroup) {
		result.getBaseList().stream().forEach(item -> {
			companyRepository.findByFinanceCompanyNumberAndDisclosureMonth(item.getFin_co_no(), item.getDcls_month())
			.map(company -> {
				Company updateCompany = Company.from(item);
				updateCompany.setFinanceGroup(financeGroup);
				updateCompany.setId(company.getId());
				updateCompany.getCompanyOptions().clear();
				addCompanyOptions(result, updateCompany);
				return companyRepository.save(updateCompany);
			}).orElseGet(() -> {
				Company company = Company.from(item);
				company.setFinanceGroup(financeGroup);
				addCompanyOptions(result, company);
				return companyRepository.save(company);
			});
		});
	}
	
	private void addCompanyOptions(FSSCompanyResult fssCompanyResult, Company company) {
		company.getCompanyOptions().addAll(fssCompanyResult.getOptionList().stream()
				.filter(option -> option.getFin_co_no().equals(company.getFinanceCompanyNumber()))
				.map(option -> CompanyOption.from(option))
				.collect(Collectors.toList())
		);
	}

}
