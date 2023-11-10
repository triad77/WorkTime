package com.kbfng.worktime.util;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.kbfng.worktime.dto.ProjectDto;
import com.kbfng.worktime.entity.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

	ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);
	
	Project toEntity(ProjectDto dto, @Context CycleAvoidingMappingContext context);
	
	ProjectDto toDto(Project entity, @Context CycleAvoidingMappingContext context);
	
	default Project toEntity(ProjectDto dto) {
		return toEntity(dto, new CycleAvoidingMappingContext());
	}
	
	default ProjectDto toDto(Project entity) {
		return toDto(entity, new CycleAvoidingMappingContext());
	}
}
