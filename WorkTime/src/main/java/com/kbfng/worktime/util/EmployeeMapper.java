package com.kbfng.worktime.util;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.kbfng.worktime.dto.EmpCreateForm;
import com.kbfng.worktime.dto.EmployeeDto;
import com.kbfng.worktime.entity.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);
	
	Employee toEntity(EmployeeDto dto, @Context CycleAvoidingMappingContext context);
	
	EmployeeDto toDto(Employee entity, @Context CycleAvoidingMappingContext context);
	
	default Employee toEntity(EmployeeDto dto) {
		return toEntity(dto, new CycleAvoidingMappingContext());
	}
	
	default EmployeeDto toDto(Employee entity) {
		return toDto(entity, new CycleAvoidingMappingContext());
	}
	
	default EmpCreateForm toForm(EmployeeDto dto) {
		
		EmpCreateForm form = new EmpCreateForm();
		form.setId(dto.getId());
		form.setEmpNo(dto.getEmpNo());
		form.setEmpNm(dto.getEmpNm());
		form.setIpAddr(dto.getIpAddr());
		form.setContract(dto.getContract());
		form.setBelngCorp(dto.getCorp().getCode());
		form.setPrjId(dto.getProject().getPrjId());
		form.setCtrcStDate(dto.getCtrcStDate());
		form.setCtrcEdDate(dto.getCtrcEdDate());
		
		return form;
	}
}
