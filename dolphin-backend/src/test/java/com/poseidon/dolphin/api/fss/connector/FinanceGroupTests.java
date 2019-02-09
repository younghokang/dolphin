package com.poseidon.dolphin.api.fss.connector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;

public class FinanceGroupTests {
	
	@Test
	public void testFindByCode() {
		for(FinanceGroup financeGroup : FinanceGroup.values()) {
			assertThat(FinanceGroup.findByCode(financeGroup.getCode())).isEqualTo(financeGroup);
		}
		try {
			FinanceGroup.findByCode("9999");
			fail();
		} catch(UnsupportedOperationException e) {}
	}

}
