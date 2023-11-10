package com.kbfng.worktime.entity;

public class EnumValue {
	private String code;
	private String value;

	public EnumValue(EnumModel enumModel) {
		code = enumModel.getCode();
		value = enumModel.getValue();
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}
}
