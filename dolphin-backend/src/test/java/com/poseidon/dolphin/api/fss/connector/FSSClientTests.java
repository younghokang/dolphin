package com.poseidon.dolphin.api.fss.connector;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.company.json.FSSCompanyResult;
import com.poseidon.dolphin.api.fss.connector.FSSClient;
import com.poseidon.dolphin.api.fss.connector.ServiceUrl;
import com.poseidon.dolphin.api.fss.result.ErrorCode;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FSSClientTests {
	
	@Value("${api.fss.key}")
	private String apiKey;
	
	//@Test
	public void testConnect() throws ClientProtocolException, URISyntaxException, IOException {
		int pageNo = 1;
		FSSCompanyResult result = FSSClient.connect(FSSCompanyResult.class, apiKey, ServiceUrl.COMPANY_SEARCH, FinanceGroup.BANK, pageNo);
		assertThat(result.getErr_cd()).isEqualTo(ErrorCode.SUCCESS.getCode());
		assertThat(result.getNow_page_no()).isEqualTo(pageNo);
	}
	
	@Test
	public void contextLoads() {
		
	}
	
}
