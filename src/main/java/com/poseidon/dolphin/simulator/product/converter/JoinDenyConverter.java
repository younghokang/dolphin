package com.poseidon.dolphin.simulator.product.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.poseidon.dolphin.simulator.product.JoinDeny;

@Converter
public class JoinDenyConverter implements AttributeConverter<JoinDeny, Integer> {

	@Override
	public Integer convertToDatabaseColumn(JoinDeny attribute) {
		return attribute.getCode();
	}

	@Override
	public JoinDeny convertToEntityAttribute(Integer dbData) {
		return JoinDeny.findByCode(dbData);
	}

}
