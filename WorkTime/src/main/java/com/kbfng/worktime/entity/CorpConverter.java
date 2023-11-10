package com.kbfng.worktime.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CorpConverter implements AttributeConverter<CorpEnum, String> {

	@Override
	public String convertToDatabaseColumn(CorpEnum corp) {
		return corp == null ? null : corp.getCode();
	}

	@Override
	public CorpEnum convertToEntityAttribute(String code) {
		return CorpEnum.find(code);
	}
}
