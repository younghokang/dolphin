package com.poseidon.dolphin.api.fss.connector;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.deposit.json.FSSDepositResult;

public class SavingConnector implements Connector<FSSDepositResult> {

	@Override
	public FSSDepositResult connect(String apiKey, FinanceGroup financeGroup, int pageNo)
			throws ClientProtocolException, URISyntaxException, IOException {
		return FSSClient.connect(FSSDepositResult.class, apiKey, ServiceUrl.SAVING_PRODUCTS_SEARCH, financeGroup, pageNo);
	}

}
