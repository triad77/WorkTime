package com.kbfng.worktime.util;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.kbfng.worktime.dto.WorkTimeDto;
import com.kbfng.worktime.entity.WorkTime;

@Mapper(componentModel = "spring")
public interface WorkTimeMapper {

	WorkTimeMapper INSTANCE = Mappers.getMapper(WorkTimeMapper.class);
	
	WorkTime toEntity(WorkTimeDto dto, @Context CycleAvoidingMappingContext context);
	
	@Mapping(target = "status", ignore = true)
	WorkTimeDto toDto(WorkTime entity, @Context CycleAvoidingMappingContext context);
	
	default WorkTime toEntity(WorkTimeDto dto) {
		return toEntity(dto, new CycleAvoidingMappingContext());
	}
	
	default WorkTimeDto toDto(WorkTime entity) {
		
		// 지각여부를 판단하기 위한 커스터마이징
		WorkTimeDto dto = toDto(entity, new CycleAvoidingMappingContext());
		dto.setStatus();
		
		return dto;
	}
}
