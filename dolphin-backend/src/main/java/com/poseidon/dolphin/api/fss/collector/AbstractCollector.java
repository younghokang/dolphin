package com.poseidon.dolphin.api.fss.collector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.connector.Connector;
import com.poseidon.dolphin.api.fss.result.Result;
import com.poseidon.dolphin.api.fss.result.service.ResultService;

public abstract class AbstractCollector<T> {
	private final ResultService resultService;
	private final Connector<?> connector;
	private final String apiKey;
	
	protected AbstractCollector(ResultService resultService, Connector<?> connector, String apiKey) {
		this.resultService = resultService;
		this.connector = connector;
		this.apiKey = apiKey;
	}

	public T collect(FinanceGroup financeGroup, int pageNo) {
		LocalDateTime connectionTime = LocalDateTime.now();
		try {
			@SuppressWarnings("unchecked")
			final T result = (T) connector.connect(apiKey, financeGroup, pageNo);
			Result resultEntity = Result.from(result);
			resultEntity.setFinanceGroup(financeGroup);
			resultEntity.setConnectionTime(connectionTime);
			resultService.save(resultEntity);
			
			store(result, financeGroup);
			
			return result;
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	abstract protected void store(T result, FinanceGroup financeGroup);

}
