package com.poseidon.dolphin.api.fss.saving.scheduler;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poseidon.dolphin.api.fss.collector.SavingCollector;
import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.connector.Connector;
import com.poseidon.dolphin.api.fss.connector.SavingConnector;
import com.poseidon.dolphin.api.fss.deposit.json.FSSDepositResult;
import com.poseidon.dolphin.api.fss.result.repository.ResultRepository;
import com.poseidon.dolphin.api.fss.saving.repository.SavingRepository;
import com.poseidon.dolphin.api.fss.saving.service.SavingService;
import com.poseidon.dolphin.simulator.product.service.ProductService;

@RestController
public class SavingScheduler {
	private final ResultRepository resultRepository;
	private final SavingRepository savingRepository;
	private final SavingService savingService;
	private final ProductService productService;
	private Connector<?> connector = new SavingConnector();
	public static final String SCHEDULER_CRON = "0 0 10 * * MON-FRI";
	
	@Value("${api.fss.key}")
	private String apiKey;
	
	public SavingScheduler(ResultRepository resultRepository, SavingRepository savingRepository, SavingService savingService, ProductService productService) {
		this.resultRepository = resultRepository;
		this.savingRepository = savingRepository;
		this.savingService = savingService;
		this.productService = productService;
	}

	public Connector<?> getConnector() {
		return connector;
	}
	public void setConnector(Connector<?> connector) {
		this.connector = connector;
	}
	
	/**
	 * Google App Engine(GAE)의 배포를 위해서 Spring Scheduler를 삭제한다.
	 * GAE는 cron 작업 파일을 등록하면 구글 서버에서 GET방식으로 호출하는 프로세스를 가진다.
	 * 따라서 Controller 형태로 변경한다.
	 * @Scheduled(cron=SCHEDULER_CRON)
	 */
	@GetMapping("/api/fss/scheduler/saving")
	public void execute() {
		SavingCollector collector = new SavingCollector(resultRepository, connector, apiKey, savingRepository);
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
		
		productService.mergeToSavings(savingService.findAllLatestDisclosureMonth());
	}
}
