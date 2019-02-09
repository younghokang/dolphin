package com.poseidon.dolphin.api.fss.common.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.poseidon.dolphin.api.fss.common.JoinDeny;

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
