package com.poseidon.dolphin.api.fss.deposit.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.poseidon.dolphin.api.fss.deposit.Deposit;
import com.poseidon.dolphin.api.fss.deposit.repository.DepositRepository;

@Service
public class DepositService {
	private final DepositRepository depositRepository;
	
	@Autowired
	public DepositService(DepositRepository depositRepository) {
		this.depositRepository = depositRepository;
	}

	public List<Deposit> findAllLatestDisclosureMonth() {
		String disclosureMonth = depositRepository.getMaxDisclosureMonth();
		if(!StringUtils.isEmpty(disclosureMonth)) {
			return depositRepository.findAllByDisclosureMonth(disclosureMonth);
		}
		return Collections.emptyList();
	}

}
