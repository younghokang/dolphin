package com.poseidon.dolphin.simulator.company.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.poseidon.dolphin.simulator.company.Area;

@Converter
public class AreaConverter implements AttributeConverter<Area, String> {

	@Override
	public String convertToDatabaseColumn(Area attribute) {
		return attribute.getAreaCode();
	}

	@Override
	public Area convertToEntityAttribute(String dbData) {
		return Area.findByAreaCode(dbData);
	}


}
