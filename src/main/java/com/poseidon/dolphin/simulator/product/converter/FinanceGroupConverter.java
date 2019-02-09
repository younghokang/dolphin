package com.poseidon.dolphin.simulator.product.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.poseidon.dolphin.simulator.product.FinanceGroup;

@Converter
public class FinanceGroupConverter implements AttributeConverter<FinanceGroup, String> {

	@Override
	public String convertToDatabaseColumn(FinanceGroup attribute) {
		return attribute.getCode();
	}

	@Override
	public FinanceGroup convertToEntityAttribute(String dbData) {
		return FinanceGroup.findByCode(dbData);
	}

}
