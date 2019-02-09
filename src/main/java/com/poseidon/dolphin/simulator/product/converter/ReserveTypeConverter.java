package com.poseidon.dolphin.simulator.product.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.poseidon.dolphin.simulator.product.ReserveType;

@Converter
public class ReserveTypeConverter implements AttributeConverter<ReserveType, String> {

	@Override
	public String convertToDatabaseColumn(ReserveType attribute) {
		return attribute.getCode();
	}

	@Override
	public ReserveType convertToEntityAttribute(String dbData) {
		return ReserveType.findByCode(dbData);
	}

}
