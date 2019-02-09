package com.poseidon.dolphin.simulator.product.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.poseidon.dolphin.simulator.product.ProductType;

@Converter
public class ProductTypeConverter implements AttributeConverter<ProductType, String> {

	@Override
	public String convertToDatabaseColumn(ProductType attribute) {
		return attribute.getCode();
	}

	@Override
	public ProductType convertToEntityAttribute(String dbData) {
		return ProductType.findByCode(dbData);
	}


}
