package com.poseidon.dolphin.api.fss.collector;

import java.util.stream.Collectors;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.company.Company;
import com.poseidon.dolphin.api.fss.company.CompanyOption;
import com.poseidon.dolphin.api.fss.company.json.FSSCompanyResult;
import com.poseidon.dolphin.api.fss.company.service.CompanyService;
import com.poseidon.dolphin.api.fss.connector.Connector;
import com.poseidon.dolphin.api.fss.result.service.ResultService;

public class CompanyCollector extends AbstractCollector<FSSCompanyResult> {
	private final CompanyService companyService;
	
	public CompanyCollector(ResultService resultService, Connector<?> connector, String apiKey, CompanyService companyService) {
		super(resultService, connector, apiKey);
		this.companyService = companyService;
	}

	@Override
	protected void store(FSSCompanyResult result, FinanceGroup financeGroup) {
		result.getBaseList().stream().forEach(item -> {
			companyService.findByFinanceCompanyNumberAndDisclosureMonth(item.getFin_co_no(), item.getDcls_month())
			.map(company -> {
				Company updateCompany = Company.from(item);
				updateCompany.setFinanceGroup(financeGroup);
				updateCompany.setId(company.getId());
				updateCompany.getCompanyOptions().clear();
				addCompanyOptions(result, updateCompany);
				return companyService.save(updateCompany);
			}).orElseGet(() -> {
				Company company = Company.from(item);
				company.setFinanceGroup(financeGroup);
				addCompanyOptions(result, company);
				return companyService.save(company);
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
