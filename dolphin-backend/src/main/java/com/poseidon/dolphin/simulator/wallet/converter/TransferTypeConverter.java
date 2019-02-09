package com.poseidon.dolphin.simulator.wallet.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.poseidon.dolphin.simulator.wallet.TransferType;

@Converter
public class TransferTypeConverter implements AttributeConverter<TransferType, String> {

	@Override
	public String convertToDatabaseColumn(TransferType attribute) {
		return attribute.getCode();
	}

	@Override
	public TransferType convertToEntityAttribute(String dbData) {
		return TransferType.findByCode(dbData);
	}

}
