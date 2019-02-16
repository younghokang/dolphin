package com.poseidon.dolphin.api.fss.collector;

import java.util.stream.Collectors;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.connector.Connector;
import com.poseidon.dolphin.api.fss.deposit.json.FSSDepositResult;
import com.poseidon.dolphin.api.fss.result.service.ResultService;
import com.poseidon.dolphin.api.fss.saving.Saving;
import com.poseidon.dolphin.api.fss.saving.SavingOption;
import com.poseidon.dolphin.api.fss.saving.service.SavingService;

public class SavingCollector extends AbstractCollector<FSSDepositResult> {
	private final SavingService savingService;
	
	public SavingCollector(ResultService resultService, Connector<?> connector, String apiKey, SavingService savingService) {
		super(resultService, connector, apiKey);
		this.savingService = savingService;
	}

	@Override
	protected void store(final FSSDepositResult result, FinanceGroup financeGroup) {
		result.getBaseList().stream().forEach(item -> {
			savingService.findByFinanceCompanyNumberAndFinanceProductCodeAndDisclosureMonth(item.getFin_co_no(), item.getFin_prdt_cd(), item.getDcls_month())
			.map(saving -> {
				Saving updateSaving = Saving.from(item);
				updateSaving.setFinanceGroup(financeGroup);
				updateSaving.setId(saving.getId());
				updateSaving.getSavingOptions().clear();
				addSavingOptions(result, updateSaving);
				return savingService.save(updateSaving);
			}).orElseGet(() -> {
				Saving saving = Saving.from(item);
				saving.setFinanceGroup(financeGroup);
				addSavingOptions(result, saving);
				return savingService.save(saving);
			});
		});
	}
	
	private void addSavingOptions(FSSDepositResult fssDepositResult, Saving saving) {
		saving.getSavingOptions().addAll(
				fssDepositResult.getOptionList().stream()
					.filter(option -> option.getFin_co_no().equals(saving.getFinanceCompanyNumber()) && option.getFin_prdt_cd().equals(saving.getFinanceProductCode()))
					.map(option -> SavingOption.from(option))
					.collect(Collectors.toList())
		);
	}

}
