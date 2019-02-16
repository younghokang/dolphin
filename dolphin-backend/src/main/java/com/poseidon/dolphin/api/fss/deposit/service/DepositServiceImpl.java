package com.poseidon.dolphin.api.fss.deposit.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.poseidon.dolphin.api.fss.deposit.Deposit;
import com.poseidon.dolphin.api.fss.deposit.repository.DepositRepository;

@Service
@Transactional
public class DepositServiceImpl implements DepositService {
	private final DepositRepository depositRepository;
	
	@Autowired
	public DepositServiceImpl(DepositRepository depositRepository) {
		this.depositRepository = depositRepository;
	}

	@Override
	public List<Deposit> findAllLatestDisclosureMonth() {
		String disclosureMonth = depositRepository.getMaxDisclosureMonth();
		if(!StringUtils.isEmpty(disclosureMonth)) {
			return depositRepository.findAllByDisclosureMonth(disclosureMonth);
		}
		return Collections.emptyList();
	}

	@Override
	public Optional<Deposit> findByFinanceCompanyNumberAndFinanceProductCodeAndDisclosureMonth(
			String financeCompanyNumber, String financeProductCode, String disclosureMonth) {
		return depositRepository.findByFinanceCompanyNumberAndFinanceProductCodeAndDisclosureMonth(financeCompanyNumber, financeProductCode, disclosureMonth);
	}

	@Override
	public Deposit save(Deposit deposit) {
		return depositRepository.save(deposit);
	}

}
