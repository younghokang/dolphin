package com.poseidon.dolphin.api.fss.saving.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.poseidon.dolphin.api.fss.saving.Saving;
import com.poseidon.dolphin.api.fss.saving.repository.SavingRepository;

@Service
public class SavingService {
	private final SavingRepository savingRepository;

	@Autowired
	public SavingService(SavingRepository savingRepository) {
		this.savingRepository = savingRepository;
	}

	public List<Saving> findAllLatestDisclosureMonth() {
		String disclosureMonth = savingRepository.getMaxDisclosureMonth();
		if(!StringUtils.isEmpty(disclosureMonth)) {
			return savingRepository.findAllByDisclosureMonth(disclosureMonth);
		}
		return Collections.emptyList();
	}
	
}
