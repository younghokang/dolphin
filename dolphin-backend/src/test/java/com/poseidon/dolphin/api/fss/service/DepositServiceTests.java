package com.poseidon.dolphin.api.fss.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.dolphin.api.fss.deposit.Deposit;
import com.poseidon.dolphin.api.fss.deposit.repository.DepositRepository;
import com.poseidon.dolphin.api.fss.deposit.service.DepositService;
import com.poseidon.dolphin.api.fss.deposit.service.DepositServiceImpl;

@RunWith(SpringRunner.class)
public class DepositServiceTests {
	private DepositService depositService;
	
	@MockBean
	private DepositRepository depositRepository;
	
	@Before
	public void setUp() {
		depositService = new DepositServiceImpl(depositRepository);
	}
	
	@Test
	public void whenNoDataThenReturnEmptyList() {
		given(depositRepository.getMaxDisclosureMonth()).willReturn(null);
		assertThat(depositService.findAllLatestDisclosureMonth()).isEmpty();
		
		String disclosureMonth = "201901";
		given(depositRepository.getMaxDisclosureMonth()).willReturn(disclosureMonth);
		
		List<Deposit> deposits = Arrays.asList(new Deposit());
		given(depositRepository.findAllByDisclosureMonth(disclosureMonth)).willReturn(deposits);
		
		assertThat(depositService.findAllLatestDisclosureMonth()).isNotEmpty();
		verify(depositRepository, times(2)).getMaxDisclosureMonth();
	}
	
}
