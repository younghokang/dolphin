package com.poseidon.dolphin.api.fss.collector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.connector.Connector;
import com.poseidon.dolphin.api.fss.result.Result;
import com.poseidon.dolphin.api.fss.result.repository.ResultRepository;

public abstract class AbstractCollector<T> {
	private final ResultRepository resultRepository;
	private final Connector<?> connector;
	private final String apiKey;
	
	protected AbstractCollector(ResultRepository resultRepository, Connector<?> connector, String apiKey) {
		this.resultRepository = resultRepository;
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
			resultRepository.save(resultEntity);
			
			store(result, financeGroup);
			
			return result;
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	abstract protected void store(T result, FinanceGroup financeGroup);

}
