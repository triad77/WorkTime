package com.kbfng.worktime.entity;

import jakarta.persistence.AttributeConverter;

public class StatConverter implements AttributeConverter<StatEnum, String> {

	@Override
	public String convertToDatabaseColumn(StatEnum stat) {
		return stat == null ? null : stat.getCode();
	}

	@Override
	public StatEnum convertToEntityAttribute(String statCd) {
		return StatEnum.find(statCd);
	}
}
