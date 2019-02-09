package com.poseidon.dolphin.api.fss.connector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.poseidon.dolphin.api.fss.result.ErrorCode;

public class ErrorCodeTests {

	@Test
	public void testFindByCode() {
		for(ErrorCode errorCode : ErrorCode.values()) {
			assertThat(ErrorCode.findByCode(errorCode.getCode())).isEqualTo(errorCode);
		}
		try {
			ErrorCode.findByCode("9999");
			fail();
		} catch(UnsupportedOperationException e) {}
	}
	
}
