package com.poseidon.dolphin.api.fss.saving.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.poseidon.dolphin.api.fss.saving.Saving;
import com.poseidon.dolphin.api.fss.saving.repository.SavingRepository;

@Service
@Transactional
public class SavingServiceImpl implements SavingService {
	private final SavingRepository savingRepository;

	@Autowired
	public SavingServiceImpl(SavingRepository savingRepository) {
		this.savingRepository = savingRepository;
	}

	@Override
	public List<Saving> findAllLatestDisclosureMonth() {
		String disclosureMonth = savingRepository.getMaxDisclosureMonth();
		if(!StringUtils.isEmpty(disclosureMonth)) {
			return savingRepository.findAllByDisclosureMonth(disclosureMonth);
		}
		return Collections.emptyList();
	}

	@Override
	public Saving save(Saving saving) {
		return savingRepository.save(saving);
	}

	@Override
	public Optional<Saving> findByFinanceCompanyNumberAndFinanceProductCodeAndDisclosureMonth(
			String financeCompanyNumber, String financeProductCode, String disclosureMonth) {
		return savingRepository.findByFinanceCompanyNumberAndFinanceProductCodeAndDisclosureMonth(financeCompanyNumber, financeProductCode, disclosureMonth);
	}
	
}
