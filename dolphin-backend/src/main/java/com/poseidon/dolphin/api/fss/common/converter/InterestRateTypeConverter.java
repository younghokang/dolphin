package com.poseidon.dolphin.api.fss.common.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.poseidon.dolphin.api.fss.common.InterestRateType;

@Converter
public class InterestRateTypeConverter implements AttributeConverter<InterestRateType, String> {

	@Override
	public String convertToDatabaseColumn(InterestRateType attribute) {
		return attribute.getCode();
	}

	@Override
	public InterestRateType convertToEntityAttribute(String dbData) {
		return InterestRateType.findByCode(dbData);
	}

}
