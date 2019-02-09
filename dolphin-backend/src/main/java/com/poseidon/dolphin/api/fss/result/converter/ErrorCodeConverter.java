package com.poseidon.dolphin.api.fss.result.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.poseidon.dolphin.api.fss.result.ErrorCode;

@Converter
public class ErrorCodeConverter implements AttributeConverter<ErrorCode, String> {

	@Override
	public String convertToDatabaseColumn(ErrorCode attribute) {
		return attribute.getCode();
	}

	@Override
	public ErrorCode convertToEntityAttribute(String dbData) {
		return ErrorCode.findByCode(dbData);
	}

}
