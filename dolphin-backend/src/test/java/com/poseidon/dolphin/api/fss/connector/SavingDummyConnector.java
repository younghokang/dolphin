package com.poseidon.dolphin.api.fss.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

import org.apache.http.client.ClientProtocolException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.connector.Connector;
import com.poseidon.dolphin.api.fss.deposit.json.FSSDepositResult;

public class SavingDummyConnector implements Connector<FSSDepositResult> {

	@Override
	public FSSDepositResult connect(String apiKey, FinanceGroup financeGroup, int pageNo)
			throws ClientProtocolException, URISyntaxException, IOException {
		Resource company = new ClassPathResource("saving.json");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(company.getInputStream(), "UTF-8"))) {
			String json = br.lines().collect(Collectors.joining());
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(json);
			Gson gson = new Gson();
			FSSDepositResult result = gson.fromJson(element.getAsJsonObject().get("result"), FSSDepositResult.class);
			return result;
		}
	}

}
