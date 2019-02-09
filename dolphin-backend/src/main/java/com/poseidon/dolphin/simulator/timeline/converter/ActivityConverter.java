package com.poseidon.dolphin.simulator.timeline.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.poseidon.dolphin.simulator.timeline.Activity;

@Converter
public class ActivityConverter implements AttributeConverter<Activity, String> {

	@Override
	public String convertToDatabaseColumn(Activity attribute) {
		return attribute.getCode();
	}

	@Override
	public Activity convertToEntityAttribute(String dbData) {
		return Activity.findByCode(dbData);
	}

}
