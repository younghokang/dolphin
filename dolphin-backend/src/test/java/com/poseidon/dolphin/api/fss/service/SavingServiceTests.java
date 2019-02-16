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

import com.poseidon.dolphin.api.fss.saving.Saving;
import com.poseidon.dolphin.api.fss.saving.repository.SavingRepository;
import com.poseidon.dolphin.api.fss.saving.service.SavingServiceImpl;

@RunWith(SpringRunner.class)
public class SavingServiceTests {
	private SavingServiceImpl savingService;
	
	@MockBean
	private SavingRepository savingRepository;
	
	@Before
	public void setUp() {
		savingService = new SavingServiceImpl(savingRepository);
	}
	
	@Test
	public void whenNoDataThenReturnEmptyList() {
		given(savingRepository.getMaxDisclosureMonth()).willReturn(null);
		assertThat(savingService.findAllLatestDisclosureMonth()).isEmpty();
		
		String disclosureMonth = "201901";
		given(savingRepository.getMaxDisclosureMonth()).willReturn(disclosureMonth);
		
		List<Saving> savings = Arrays.asList(new Saving());
		given(savingRepository.findAllByDisclosureMonth(disclosureMonth)).willReturn(savings);
		
		assertThat(savingService.findAllLatestDisclosureMonth()).isNotEmpty();
		verify(savingRepository, times(2)).getMaxDisclosureMonth();
	}
	
}
