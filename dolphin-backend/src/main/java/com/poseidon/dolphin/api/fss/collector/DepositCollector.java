package com.poseidon.dolphin.api.fss.collector;

import java.util.stream.Collectors;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.connector.Connector;
import com.poseidon.dolphin.api.fss.deposit.Deposit;
import com.poseidon.dolphin.api.fss.deposit.DepositOption;
import com.poseidon.dolphin.api.fss.deposit.json.FSSDepositResult;
import com.poseidon.dolphin.api.fss.deposit.service.DepositService;
import com.poseidon.dolphin.api.fss.result.service.ResultService;

public class DepositCollector extends AbstractCollector<FSSDepositResult> {
	private final DepositService depositService;
	
	public DepositCollector(ResultService resultService, Connector<?> connector, String apiKey, DepositService depositService) {
		super(resultService, connector, apiKey);
		this.depositService = depositService;
	}

	@Override
	protected void store(final FSSDepositResult result, FinanceGroup financeGroup) {
		result.getBaseList().stream().forEach(item -> {
			depositService.findByFinanceCompanyNumberAndFinanceProductCodeAndDisclosureMonth(item.getFin_co_no(), item.getFin_prdt_cd(), item.getDcls_month())
			.map(deposit -> {
				Deposit updateDeposit = Deposit.from(item);
				updateDeposit.setFinanceGroup(financeGroup);
				updateDeposit.setId(deposit.getId());
				updateDeposit.getDepositOptions().clear();
				addDepositOptions(result, updateDeposit);
				return depositService.save(updateDeposit);
			}).orElseGet(() -> {
				Deposit deposit = Deposit.from(item);
				deposit.setFinanceGroup(financeGroup);
				addDepositOptions(result, deposit);
				return depositService.save(deposit);
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
