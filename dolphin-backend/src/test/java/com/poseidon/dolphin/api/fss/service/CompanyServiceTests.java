package com.poseidon.dolphin.api.fss.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.dolphin.api.fss.company.Company;
import com.poseidon.dolphin.api.fss.company.repository.CompanyRepository;
import com.poseidon.dolphin.api.fss.company.service.CompanyService;
import com.poseidon.dolphin.api.fss.company.service.CompanyServiceImpl;

@RunWith(SpringRunner.class)
public class CompanyServiceTests {
	private CompanyService companyService;
	
	@MockBean
	private CompanyRepository companyRepository;
	
	@Before
	public void setUp() {
		companyService = new CompanyServiceImpl(companyRepository);
	}
	
	@Test
	public void whenNoDataThenReturnEmptyList() {
		given(companyRepository.getMaxDisclosureMonth()).willReturn(null);
		assertThat(companyService.findAllLatestDisclosureMonth()).isEmpty();
		
		String disclosureMonth = "201901";
		given(companyRepository.getMaxDisclosureMonth()).willReturn(disclosureMonth);
		
		List<Company> companies = Arrays.asList(new Company());
		given(companyRepository.findAllByDisclosureMonth(disclosureMonth)).willReturn(companies);
		
		assertThat(companyService.findAllLatestDisclosureMonth()).isNotEmpty();
		verify(companyRepository, times(2)).getMaxDisclosureMonth();
	}
	
	@Test
	public void givenFinanceCompanyNumberThenReturnLatestDisclosureMonthData() {
		String financeCompanyNumber = "AAAA";
		given(companyRepository.getMaxDisclosureMonth()).willReturn("");
		assertThat(companyService.findByLatestDisclosureMonthAndFinanceCompanyNumber(financeCompanyNumber)).isEmpty();
		
		Company company = new Company();
		given(companyRepository.getMaxDisclosureMonth()).willReturn("201901");
		given(companyRepository.findByFinanceCompanyNumberAndDisclosureMonth(anyString(), anyString())).willReturn(Optional.of(company));
		assertThat(companyService.findByLatestDisclosureMonthAndFinanceCompanyNumber(financeCompanyNumber)).isNotEmpty();
		
		verify(companyRepository, times(2)).getMaxDisclosureMonth();
		verify(companyRepository, times(1)).findByFinanceCompanyNumberAndDisclosureMonth(anyString(), anyString());
	}
	
}
