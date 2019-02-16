package com.poseidon.dolphin.api.fss.deposit.scheduler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poseidon.dolphin.api.cache.FrontendCacheClient;
import com.poseidon.dolphin.api.fss.collector.DepositCollector;
import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.connector.Connector;
import com.poseidon.dolphin.api.fss.connector.DepositConnector;
import com.poseidon.dolphin.api.fss.deposit.json.FSSDepositResult;
import com.poseidon.dolphin.api.fss.deposit.service.DepositService;
import com.poseidon.dolphin.api.fss.result.service.ResultService;
import com.poseidon.dolphin.simulator.product.ProductType;
import com.poseidon.dolphin.simulator.product.service.ProductService;

@RestController
public class DepositScheduler {
	private final ResultService resultService;
	private final DepositService depositService;
	private final ProductService productService;
	private final FrontendCacheClient cacheClient;
	private Connector<?> connector = new DepositConnector();
	public static final String SCHEDULER_CRON = "0 0 8 * * MON-FRI";
	
	@Value("${api.fss.key}")
	private String apiKey;
	
	public DepositScheduler(ResultService resultService, DepositService depositService, ProductService productService, FrontendCacheClient cacheClient) {
		this.resultService = resultService;
		this.depositService = depositService;
		this.productService = productService;
		this.cacheClient = cacheClient;
	}
	
	public Connector<?> getConnector() {
		return connector;
	}
	public void setConnector(Connector<?> connector) {
		this.connector = connector;
	}

	//@Scheduled(cron=SCHEDULER_CRON)
	@GetMapping("/api/fss/scheduler/deposit")
	public void execute() {
		DepositCollector collector = new DepositCollector(resultService, connector, apiKey, depositService);
		Arrays.stream(FinanceGroup.values()).forEach(financeGroup -> {
			int pageNo = 1;
			int maxPageNo = 1;
			FSSDepositResult fssDepositResult = collector.collect(financeGroup, maxPageNo);
			maxPageNo = fssDepositResult.getMax_page_no();
			
			while(pageNo < maxPageNo) {
				pageNo++;
				fssDepositResult = collector.collect(financeGroup, pageNo);
			}
		});
		
		productService.mergeToDeposits(depositService.findAllLatestDisclosureMonth());
		try {
			cacheClient.productsCachePut(ProductType.FIXED_DEPOSIT);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
