package com.poseidon.dolphin.api.fss.connector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.poseidon.dolphin.api.fss.result.ProductDivision;

public class ProductDivisionTests {
	
	@Test
	public void testFindByCode() {
		for(ProductDivision productDivision : ProductDivision.values()) {
			assertThat(ProductDivision.findByCode(productDivision.getCode())).isEqualTo(productDivision);
		}
		try {
			ProductDivision.findByCode("9999");
			fail();
		} catch(UnsupportedOperationException e) {}
	}

}
