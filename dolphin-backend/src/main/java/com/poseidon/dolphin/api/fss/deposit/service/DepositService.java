package com.poseidon.dolphin.api.fss.deposit.service;

import java.util.List;
import java.util.Optional;

import com.poseidon.dolphin.api.fss.deposit.Deposit;

public interface DepositService {
	List<Deposit> findAllLatestDisclosureMonth();
	Optional<Deposit> findByFinanceCompanyNumberAndFinanceProductCodeAndDisclosureMonth(String financeCompanyNumber, String financeProductCode, String disclosureMonth);
	Deposit save(Deposit deposit);
}
