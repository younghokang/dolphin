package com.poseidon.dolphin.simulator.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.poseidon.dolphin.simulator.account.Contract;
import com.poseidon.dolphin.simulator.calculator.InterestType;
import com.poseidon.dolphin.simulator.product.Interest;
import com.poseidon.dolphin.simulator.product.ProductType;

public class InterestTypeTests {
	
	@Test
	public void testFindByContract() {
		Contract contract = new Contract();
		contract.setProductType(ProductType.INSTALLMENT_SAVING);
		contract.setInterest(Interest.DAY_SIMPLE);
		
		assertThat(InterestType.findByContract(contract)).isEqualTo(InterestType.INSTALLMENT_SAVING_DAYILY_SIMPLE);
		
		contract.setInterest(Interest.MONTH_SIMPLE);
		assertThat(InterestType.findByContract(contract)).isEqualTo(InterestType.INSTALLMENT_SAVING_MONTHLY_SIMPLE);
		
		contract.setInterest(Interest.MONTH_COMPOUND);
		assertThat(InterestType.findByContract(contract)).isEqualTo(InterestType.INSTALLMENT_SAVING_MONTHLY_COMPOUND);
		
		contract.setProductType(ProductType.FIXED_DEPOSIT);
		contract.setInterest(Interest.DAY_SIMPLE);
		assertThat(InterestType.findByContract(contract)).isEqualTo(InterestType.FIXED_DEPOSIT_DAILY_SIMPLE);
		
		contract.setInterest(Interest.MONTH_SIMPLE);
		assertThat(InterestType.findByContract(contract)).isEqualTo(InterestType.FIXED_DEPOSIT_MONTHLY_SIMPLE);
		
		contract.setInterest(Interest.MONTH_COMPOUND);
		assertThat(InterestType.findByContract(contract)).isEqualTo(InterestType.FIXED_DEPOSIT_MONTHLY_COMPOUND);
	}

}
