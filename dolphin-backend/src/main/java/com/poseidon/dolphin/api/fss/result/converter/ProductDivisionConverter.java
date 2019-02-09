package com.poseidon.dolphin.api.fss.result.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.poseidon.dolphin.api.fss.result.ProductDivision;

@Converter
public class ProductDivisionConverter implements AttributeConverter<ProductDivision, String> {

	@Override
	public String convertToDatabaseColumn(ProductDivision attribute) {
		return attribute.getCode();
	}

	@Override
	public ProductDivision convertToEntityAttribute(String dbData) {
		return ProductDivision.findByCode(dbData);
	}

}
