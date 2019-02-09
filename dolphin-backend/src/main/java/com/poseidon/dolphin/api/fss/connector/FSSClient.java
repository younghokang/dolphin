package com.poseidon.dolphin.api.fss.connector;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.poseidon.dolphin.api.fss.common.FinanceGroup;

public class FSSClient {
	
	private FSSClient() {}
	
	private static JsonElement connect(String apiKey, ServiceUrl serviceUrl, FinanceGroup financeGroup, int pageNo) throws URISyntaxException, ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		URI uri = new URIBuilder()
				.setScheme("http")
				.setHost("finlife.fss.or.kr")
				.setPath("/finlifeapi/" + serviceUrl.getUrl() + ".json")
				.setParameter("auth", apiKey)
				.setParameter("topFinGrpNo", financeGroup.getCode())
				.setParameter("pageNo", Integer.toString(pageNo))
				.build();
		HttpGet httpGet = new HttpGet(uri);
		CloseableHttpResponse response = httpClient.execute(httpGet);
		try {
			String json = EntityUtils.toString(response.getEntity(), "UTF-8");
			return parse(json);
		} finally {
			response.close();
		}
	}

	private static JsonElement parse(String json) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(json);
		return element.getAsJsonObject().get("result");
	}
	
	public static <T> T connect(Class<T> classOfT, String apiKey, ServiceUrl serviceUrl, FinanceGroup financeGroup, int pageNo) throws ClientProtocolException, URISyntaxException, IOException {
		Gson gson = new Gson();
		JsonElement element = connect(apiKey, serviceUrl, financeGroup, pageNo);
		return gson.fromJson(element, classOfT);
	}
	
}
