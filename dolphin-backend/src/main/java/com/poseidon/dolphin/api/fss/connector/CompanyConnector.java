package com.poseidon.dolphin.api.fss.connector;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.company.json.FSSCompanyResult;

public class CompanyConnector implements Connector<FSSCompanyResult> {

	@Override
	public FSSCompanyResult connect(String apiKey, FinanceGroup financeGroup, int pageNo)
			throws ClientProtocolException, URISyntaxException, IOException {
		return FSSClient.connect(FSSCompanyResult.class, apiKey, ServiceUrl.COMPANY_SEARCH, financeGroup, pageNo);
	}

}
