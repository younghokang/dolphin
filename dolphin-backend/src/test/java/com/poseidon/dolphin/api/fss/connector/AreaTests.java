package com.poseidon.dolphin.api.fss.connector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.poseidon.dolphin.api.fss.company.Area;

public class AreaTests {

	@Test
	public void testFindByAreaCode() {
		for(Area area : Area.values()) {
			assertThat(Area.findByAreaCode(area.getAreaCode())).isEqualTo(area);
		}
		try {
			Area.findByAreaCode("99");
			fail();
		} catch(UnsupportedOperationException e) {}
	}
	
}
