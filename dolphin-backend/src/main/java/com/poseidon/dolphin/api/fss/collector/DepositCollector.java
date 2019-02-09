package com.poseidon.dolphin.api.fss.collector;

import java.util.stream.Collectors;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.connector.Connector;
import com.poseidon.dolphin.api.fss.deposit.Deposit;
import com.poseidon.dolphin.api.fss.deposit.DepositOption;
import com.poseidon.dolphin.api.fss.deposit.json.FSSDepositResult;
import com.poseidon.dolphin.api.fss.deposit.repository.DepositRepository;
import com.poseidon.dolphin.api.fss.result.repository.ResultRepository;

public class DepositCollector extends AbstractCollector<FSSDepositResult> {
	private final DepositRepository depositRepository;
	
	public DepositCollector(ResultRepository resultRepository, Connector<?> connector, String apiKey, DepositRepository depositRepository) {
		super(resultRepository, connector, apiKey);
		this.depositRepository = depositRepository;
	}

	@Override
	protected void store(final FSSDepositResult result, FinanceGroup financeGroup) {
		result.getBaseList().stream().forEach(item -> {
			depositRepository.findByFinanceCompanyNumberAndFinanceProductCodeAndDisclosureMonth(item.getFin_co_no(), item.getFin_prdt_cd(), item.getDcls_month())
			.map(deposit -> {
				Deposit updateDeposit = Deposit.from(item);
				updateDeposit.setFinanceGroup(financeGroup);
				updateDeposit.setId(deposit.getId());
				updateDeposit.getDepositOptions().clear();
				addDepositOptions(result, updateDeposit);
				return depositRepository.save(updateDeposit);
			}).orElseGet(() -> {
				Deposit deposit = Deposit.from(item);
				deposit.setFinanceGroup(financeGroup);
				addDepositOptions(result, deposit);
				return depositRepository.save(deposit);
			});
		});
	}
	
	private void addDepositOptions(FSSDepositResult fssDepositResult, Deposit deposit) {
		deposit.getDepositOptions().addAll(
				fssDepositResult.getOptionList().stream()
					.filter(option -> option.getFin_co_no().equals(deposit.getFinanceCompanyNumber()) && option.getFin_prdt_cd().equals(deposit.getFinanceProductCode()))
					.map(option -> DepositOption.from(option))
					.collect(Collectors.toList()));
	}

}
