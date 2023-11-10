package com.kbfng.worktime.entity;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;

import lombok.Getter;

@Getter
public enum StatEnum implements EnumModel{

	WHOLE("0", "전체"),
	READY("1", "준비"),
	PRGRS("2", "진행"),
	  END("3", "종료")
	;
	
	private String code;
	private String value;
	
	StatEnum(String statCd, String statNm){
		this.code = statCd;
		this.value = statNm;
	}
	
	private static final ImmutableMap<String, StatEnum> codes = 
			ImmutableMap.copyOf(Stream.of(values())
					.collect(Collectors.toMap(StatEnum::getCode, Function.identity())));
	
	public static StatEnum find(String code) {
		return Optional.ofNullable(codes.get(code)).orElse(null);
	}
}
