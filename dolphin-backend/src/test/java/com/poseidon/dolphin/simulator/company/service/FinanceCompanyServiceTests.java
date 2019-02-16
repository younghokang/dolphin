package com.poseidon.dolphin.simulator.company.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.company.Area;
import com.poseidon.dolphin.api.fss.company.Company;
import com.poseidon.dolphin.api.fss.company.CompanyOption;
import com.poseidon.dolphin.simulator.company.FinanceCompany;
import com.poseidon.dolphin.simulator.company.FinanceCompanyCommand;
import com.poseidon.dolphin.simulator.company.repository.FinanceCompanyRepository;

@RunWith(SpringRunner.class)
public class FinanceCompanyServiceTests {
	private FinanceCompanyService financeCompanyService;
	
	@MockBean
	private FinanceCompanyRepository financeCompanyRepository;
	
	@Before
	public void setUp() {
		financeCompanyService = new FinanceCompanyServiceImpl(financeCompanyRepository);
	}
	
	@Test
	public void givenCompanyListThenMerge() {
		FinanceCompany financeCompany = new FinanceCompany();
		given(financeCompanyRepository.findByCode(anyString())).willReturn(Optional.empty());
		given(financeCompanyRepository.save(any(FinanceCompany.class))).willReturn(financeCompany);
		
		List<Company> companies = new ArrayList<>();
		Company company = new Company();
		company.setDisclosureChargeMan("ABCD");
		company.setDisclosureMonth("201901");
		company.setFinanceCompanyNumber("AAAA");
		company.setFinanceGroup(FinanceGroup.BANK);
		company.setHomepageUrl("www.google.com");
		company.setKoreanFinanceCompanyName("한국은행");
		company.setCallCenterTel("1588-1588");
		
		CompanyOption companyOption = new CompanyOption();
		companyOption.setArea(Area.BUSAN);
		companyOption.setAreaName("부산");
		companyOption.setDisclosureMonth("201901");
		companyOption.setFinanceCompanyNumber("AAAA");
		companyOption.setExistsYn("Y");
		company.getCompanyOptions().add(companyOption);
		companies.add(company);
		
		assertThat(financeCompanyService.mergeTo(companies)).isNotEmpty();
		
		verify(financeCompanyRepository, times(companies.size())).findByCode(anyString());
		verify(financeCompanyRepository, times(companies.size())).save(any(FinanceCompany.class));
	}
	
	@Test
	public void givenFinanceCompanyCommandThenSave() {
		FinanceCompanyCommand fcc = new FinanceCompanyCommand();
		given(financeCompanyRepository.findById(anyLong())).willReturn(Optional.empty());
		try {
			financeCompanyService.save(fcc);
			fail();
		} catch(NullPointerException ex) {}
		
		FinanceCompany financeCompany = new FinanceCompany();
		given(financeCompanyRepository.findById(anyLong())).willReturn(Optional.of(financeCompany));
		given(financeCompanyRepository.save(any(FinanceCompany.class))).willReturn(financeCompany);
		
		assertThat(financeCompanyService.save(fcc)).isEqualToComparingFieldByField(financeCompany);
		
		verify(financeCompanyRepository, times(2)).findById(anyLong());
		verify(financeCompanyRepository, times(1)).save(any(FinanceCompany.class));
	}
	
}
