package com.poseidon.dolphin.api.fss.company.scheduler;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poseidon.dolphin.api.fss.collector.CompanyCollector;
import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.company.json.FSSCompanyResult;
import com.poseidon.dolphin.api.fss.company.repository.CompanyRepository;
import com.poseidon.dolphin.api.fss.company.service.CompanyService;
import com.poseidon.dolphin.api.fss.connector.CompanyConnector;
import com.poseidon.dolphin.api.fss.connector.Connector;
import com.poseidon.dolphin.api.fss.result.repository.ResultRepository;
import com.poseidon.dolphin.simulator.company.service.FinanceCompanyService;

@RestController
public class CompanyScheduler {
	private final ResultRepository resultRepository;
	private final CompanyRepository companyRepository;
	private final CompanyService companyService;
	private final FinanceCompanyService financeCompanyService;
	private Connector<?> connector = new CompanyConnector();
	public static final String SCHEDULER_CRON = "0 0 9 * * MON-FRI";
	
	@Value("${api.fss.key}")
	private String apiKey;
	
	public CompanyScheduler(ResultRepository resultRepository, CompanyRepository companyRepository, CompanyService companyService, FinanceCompanyService financeCompanyService) {
		this.resultRepository = resultRepository;
		this.companyRepository = companyRepository;
		this.companyService = companyService;
		this.financeCompanyService = financeCompanyService;
	}
	
	public Connector<?> getConnector() {
		return connector;
	}
	public void setConnector(Connector<?> connector) {
		this.connector = connector;
	}

	//@Scheduled(cron=SCHEDULER_CRON)
	@GetMapping("/api/fss/scheduler/company")
	public void execute() {
		CompanyCollector collector = new CompanyCollector(resultRepository, connector, apiKey, companyRepository);
		Arrays.stream(FinanceGroup.values()).forEach(financeGroup -> {
			int pageNo = 1;
			int maxPageNo = 1;
			FSSCompanyResult fssCompanyResult = collector.collect(financeGroup, maxPageNo);
			maxPageNo = fssCompanyResult.getMax_page_no();
			
			while(pageNo < maxPageNo) {
				pageNo++;
				fssCompanyResult = collector.collect(financeGroup, pageNo);
			}
		});
		
		financeCompanyService.mergeTo(companyService.findAllLatestDisclosureMonth());
	}

}
