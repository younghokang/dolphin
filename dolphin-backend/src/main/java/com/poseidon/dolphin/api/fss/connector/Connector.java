package com.poseidon.dolphin.api.fss.connector;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;

public interface Connector<T> {
	T connect(String apiKey, FinanceGroup financeGroup, int pageNo) throws ClientProtocolException, URISyntaxException, IOException;

}
