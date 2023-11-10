package com.kbfng.worktime.entity;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;

import lombok.Getter;

@Getter
public enum CorpEnum implements EnumModel{

	KBFG("01", "KB금융지주"),
	KBBK("02", "KB국민은행"),
	KBCD("03", "KB국민카드"),
	KBIN("04", "KB손해보험"),
	KBDS("11", "KB데이타시스템"),
	GNBI("12", "지엔비아이텍"),
	ITCR("13", "아이티커리어즈")
	;
	
	private static final ImmutableMap<String, CorpEnum> codes = 
			ImmutableMap.copyOf(Stream.of(values())
					.collect(Collectors.toMap(CorpEnum::getCode, Function.identity())));
	
	private String code;
	private String value;
	
	CorpEnum(String code, String value){
		this.code = code;
		this.value = value;
	}
	
	public static CorpEnum find(String code) {
		return Optional.ofNullable(codes.get(code)).orElse(null);
	}
	
	/**
	 * EnumValidator를 사용한 값 비교를 위해 재정의
	 */
	@Override
	public String toString() {
		return this.code;
	}
}
