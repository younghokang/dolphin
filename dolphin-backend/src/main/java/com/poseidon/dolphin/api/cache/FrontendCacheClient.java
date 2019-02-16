package com.poseidon.dolphin.api.cache;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.poseidon.dolphin.simulator.product.ProductType;

@Component
public class FrontendCacheClient {
	
	@Value("${api.frontend.host}")
	private String host;
	
	@Value("${api.frontend.cache.products.path}")
	private String path;
	
	public HttpStatus productsCachePut(ProductType productType) throws URISyntaxException, ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		URI uri = new URIBuilder()
				.setScheme("http")
				.setHost(host)
				.setPath(path + productType)
				.build();
		HttpGet httpGet = new HttpGet(uri);
		CloseableHttpResponse response = httpClient.execute(httpGet);
		HttpStatus httpStatus;
		try {
			httpStatus = HttpStatus.resolve(response.getStatusLine().getStatusCode());
		} finally {
			response.close();
		}
		return httpStatus;
	}
	
}
