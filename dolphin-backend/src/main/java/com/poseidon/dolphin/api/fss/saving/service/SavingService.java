package com.poseidon.dolphin.api.fss.saving.service;

import java.util.List;
import java.util.Optional;

import com.poseidon.dolphin.api.fss.saving.Saving;

public interface SavingService {
	List<Saving> findAllLatestDisclosureMonth();
	Saving save(Saving saving);
	Optional<Saving> findByFinanceCompanyNumberAndFinanceProductCodeAndDisclosureMonth(String financeCompanyNumber, String financeProductCode, String disclosureMonth);
}
