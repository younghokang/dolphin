package com.poseidon.dolphin.simulator.company.service;

import java.util.List;

import com.poseidon.dolphin.api.fss.company.Company;
import com.poseidon.dolphin.simulator.company.FinanceCompany;
import com.poseidon.dolphin.simulator.company.FinanceCompanyCommand;

public interface FinanceCompanyService {
	List<FinanceCompany> mergeTo(List<Company> companies);
	FinanceCompany save(FinanceCompanyCommand financeCompanyCommand);
}
