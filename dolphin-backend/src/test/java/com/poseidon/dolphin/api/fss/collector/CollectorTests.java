package com.poseidon.dolphin.api.fss.collector;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.dolphin.api.fss.collector.CompanyCollector;
import com.poseidon.dolphin.api.fss.collector.DepositCollector;
import com.poseidon.dolphin.api.fss.collector.SavingCollector;
import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.company.Company;
import com.poseidon.dolphin.api.fss.company.json.FSSCompanyResult;
import com.poseidon.dolphin.api.fss.company.repository.CompanyRepository;
import com.poseidon.dolphin.api.fss.connector.CompanyDummyConnector;
import com.poseidon.dolphin.api.fss.connector.DepositDummyConnector;
import com.poseidon.dolphin.api.fss.connector.SavingDummyConnector;
import com.poseidon.dolphin.api.fss.deposit.Deposit;
import com.poseidon.dolphin.api.fss.deposit.json.FSSDepositResult;
import com.poseidon.dolphin.api.fss.deposit.repository.DepositRepository;
import com.poseidon.dolphin.api.fss.result.ErrorCode;
import com.poseidon.dolphin.api.fss.result.repository.ResultRepository;
import com.poseidon.dolphin.api.fss.saving.Saving;
import com.poseidon.dolphin.api.fss.saving.repository.SavingRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CollectorTests {
	
	@Value("${api.fss.key}")
	private String apiKey;
	
	@Autowired
	private ResultRepository resultRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private SavingRepository savingRepository;
	
	@Autowired
	private DepositRepository depositRepository;
	
	@Test
	public void testCompanyCollector() {
		CompanyCollector collector = new CompanyCollector(resultRepository, new CompanyDummyConnector(), apiKey, companyRepository);
		FSSCompanyResult fssCompanyResult = collector.collect(FinanceGroup.BANK, 1);
		assertThat(fssCompanyResult.getErr_cd()).isEqualTo(ErrorCode.SUCCESS.getCode());
		Company company = companyRepository.findByFinanceCompanyNumberAndDisclosureMonth("0010001", "201901").orElse(null);
		assertThat(company).isNotNull();
		assertThat(company.getCompanyOptions()).isNotEmpty();
		assertThat(company.getCompanyOptions().size()).isEqualTo(17);
	}
	
	@Test
	public void testSavingCollector() {
		SavingCollector collector = new SavingCollector(resultRepository, new SavingDummyConnector(), apiKey, savingRepository);
		FSSDepositResult fssDepositResult = collector.collect(FinanceGroup.BANK, 1);
		assertThat(fssDepositResult.getErr_cd()).isEqualTo(ErrorCode.SUCCESS.getCode());
		Saving saving = savingRepository.findByFinanceCompanyNumberAndFinanceProductCodeAndDisclosureMonth("0010002", "00266451", "201901").orElse(null);
		assertThat(saving).isNotNull();
		assertThat(saving.getSavingOptions()).isNotEmpty();
		assertThat(saving.getSavingOptions().size()).isEqualTo(8);
	}
	
	@Test
	public void testDepositCollector() {
		DepositCollector collector = new DepositCollector(resultRepository, new DepositDummyConnector(), apiKey, depositRepository);
		FSSDepositResult fssDepositResult = collector.collect(FinanceGroup.BANK, 1);
		assertThat(fssDepositResult.getErr_cd()).isEqualTo(ErrorCode.SUCCESS.getCode());
		Deposit deposit = depositRepository.findByFinanceCompanyNumberAndFinanceProductCodeAndDisclosureMonth("0010345", "HK00001", "201901").orElse(null);
		assertThat(deposit).isNotNull();
		assertThat(deposit.getDepositOptions()).isNotEmpty();
		assertThat(deposit.getDepositOptions().size()).isEqualTo(8);
	}
	
	@Test
	public void findAllByMaximumDisclosureMonth() {
		CompanyCollector companyCollector = new CompanyCollector(resultRepository, new CompanyDummyConnector(), apiKey, companyRepository);
		companyCollector.collect(FinanceGroup.BANK, 1);
		
		String companyDisclosureMonth = companyRepository.getMaxDisclosureMonth();
		assertThat(companyDisclosureMonth).isEqualTo("201901");
		
		List<Company> companyList = companyRepository.findAllByDisclosureMonth(companyDisclosureMonth);
		assertThat(companyList).allMatch(company -> company.getDisclosureMonth().equals(companyDisclosureMonth));
		
		SavingCollector savingCollector = new SavingCollector(resultRepository, new SavingDummyConnector(), apiKey, savingRepository);
		savingCollector.collect(FinanceGroup.BANK, 1);
		
		String savingDisclosureMonth = savingRepository.getMaxDisclosureMonth();
		assertThat(savingDisclosureMonth).isEqualTo("201901");
		List<Saving> savingList = savingRepository.findAllByDisclosureMonth(savingDisclosureMonth);
		assertThat(savingList).allMatch(saving -> saving.getDisclosureMonth().equals(savingDisclosureMonth));
		
		DepositCollector depositCollector = new DepositCollector(resultRepository, new DepositDummyConnector(), apiKey, depositRepository);
		depositCollector.collect(FinanceGroup.BANK, 1);
		
		String depositDisclosureMonth = depositRepository.getMaxDisclosureMonth();
		assertThat(depositDisclosureMonth).isEqualTo("201901");
		
		List<Deposit> depositList = depositRepository.findAllByDisclosureMonth(depositDisclosureMonth);
		assertThat(depositList).allMatch(deposit -> deposit.getDisclosureMonth().equals(depositDisclosureMonth));
	}
	
}
