package com.kbfng.worktime.dto;

import com.kbfng.worktime.entity.CorpEnum;
import com.kbfng.worktime.entity.Project;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class EmployeeDto {

	private Integer id;
	
	private String empNo;
	
	private String ipAddr;
	
	private String empNm;
	
	private String contract;
	
	private CorpEnum corp;

	private Project project;
	
	private String ctrcStDate;
	
	private String ctrcEdDate;
}
