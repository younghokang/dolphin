package com.poseidon.dolphin.api.fss.collector;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.company.Company;
import com.poseidon.dolphin.api.fss.company.json.FSSCompanyResult;
import com.poseidon.dolphin.api.fss.company.repository.CompanyRepository;
import com.poseidon.dolphin.api.fss.company.service.CompanyService;
import com.poseidon.dolphin.api.fss.connector.CompanyDummyConnector;
import com.poseidon.dolphin.api.fss.connector.DepositDummyConnector;
import com.poseidon.dolphin.api.fss.connector.SavingDummyConnector;
import com.poseidon.dolphin.api.fss.deposit.Deposit;
import com.poseidon.dolphin.api.fss.deposit.json.FSSDepositResult;
import com.poseidon.dolphin.api.fss.deposit.repository.DepositRepository;
import com.poseidon.dolphin.api.fss.deposit.service.DepositService;
import com.poseidon.dolphin.api.fss.result.ErrorCode;
import com.poseidon.dolphin.api.fss.result.service.ResultService;
import com.poseidon.dolphin.api.fss.saving.Saving;
import com.poseidon.dolphin.api.fss.saving.repository.SavingRepository;
import com.poseidon.dolphin.api.fss.saving.service.SavingService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CollectorTests {
	
	@Value("${api.fss.key}")
	private String apiKey;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private SavingRepository savingRepository;
	
	@Autowired
	private DepositRepository depositRepository;
	
	@Autowired
	private ResultService resultService;
	
	@Autowired
	private SavingService savingService;
	
	@Autowired
	private DepositService depositService;
	
	@Autowired
	private CompanyService companyService;
	
	@Test
	public void testCompanyCollector() {
		CompanyCollector collector = new CompanyCollector(resultService, new CompanyDummyConnector(), apiKey, companyService);
		FSSCompanyResult fssCompanyResult = collector.collect(FinanceGroup.BANK, 1);
		assertThat(fssCompanyResult.getErr_cd()).isEqualTo(ErrorCode.SUCCESS.getCode());
		Company company = companyService.findByFinanceCompanyNumberAndDisclosureMonth("0010001", "201901").orElse(null);
		assertThat(company).isNotNull();
		assertThat(company.getCompanyOptions()).isNotEmpty();
		assertThat(company.getCompanyOptions().size()).isEqualTo(17);
	}
	
	@Test
	public void testSavingCollector() {
		SavingCollector collector = new SavingCollector(resultService, new SavingDummyConnector(), apiKey, savingService);
		FSSDepositResult fssDepositResult = collector.collect(FinanceGroup.BANK, 1);
		assertThat(fssDepositResult.getErr_cd()).isEqualTo(ErrorCode.SUCCESS.getCode());
		Saving saving = savingRepository.findByFinanceCompanyNumberAndFinanceProductCodeAndDisclosureMonth("0010002", "00266451", "201901").orElse(null);
		assertThat(saving).isNotNull();
		assertThat(saving.getSavingOptions()).isNotEmpty();
		assertThat(saving.getSavingOptions().size()).isEqualTo(8);
	}
	
	@Test
	public void testDepositCollector() {
		DepositCollector collector = new DepositCollector(resultService, new DepositDummyConnector(), apiKey, depositService);
		FSSDepositResult fssDepositResult = collector.collect(FinanceGroup.BANK, 1);
		assertThat(fssDepositResult.getErr_cd()).isEqualTo(ErrorCode.SUCCESS.getCode());
		Deposit deposit = depositRepository.findByFinanceCompanyNumberAndFinanceProductCodeAndDisclosureMonth("0010345", "HK00001", "201901").orElse(null);
		assertThat(deposit).isNotNull();
		assertThat(deposit.getDepositOptions()).isNotEmpty();
		assertThat(deposit.getDepositOptions().size()).isEqualTo(8);
	}
	
	@Test
	public void findAllByMaximumDisclosureMonth() {
		CompanyCollector companyCollector = new CompanyCollector(resultService, new CompanyDummyConnector(), apiKey, companyService);
		companyCollector.collect(FinanceGroup.BANK, 1);
		
		String companyDisclosureMonth = companyRepository.getMaxDisclosureMonth();
		assertThat(companyDisclosureMonth).isEqualTo("201901");
		
		List<Company> companyList = companyRepository.findAllByDisclosureMonth(companyDisclosureMonth);
		assertThat(companyList).allMatch(company -> company.getDisclosureMonth().equals(companyDisclosureMonth));
		
		SavingCollector savingCollector = new SavingCollector(resultService, new SavingDummyConnector(), apiKey, savingService);
		savingCollector.collect(FinanceGroup.BANK, 1);
		
		String savingDisclosureMonth = savingRepository.getMaxDisclosureMonth();
		assertThat(savingDisclosureMonth).isEqualTo("201901");
		List<Saving> savingList = savingRepository.findAllByDisclosureMonth(savingDisclosureMonth);
		assertThat(savingList).allMatch(saving -> saving.getDisclosureMonth().equals(savingDisclosureMonth));
		
		DepositCollector depositCollector = new DepositCollector(resultService, new DepositDummyConnector(), apiKey, depositService);
		depositCollector.collect(FinanceGroup.BANK, 1);
		
		String depositDisclosureMonth = depositRepository.getMaxDisclosureMonth();
		assertThat(depositDisclosureMonth).isEqualTo("201901");
		
		List<Deposit> depositList = depositRepository.findAllByDisclosureMonth(depositDisclosureMonth);
		assertThat(depositList).allMatch(deposit -> deposit.getDisclosureMonth().equals(depositDisclosureMonth));
	}
	
}
