package com.kbfng.worktime.dto;

import java.util.List;

import com.kbfng.worktime.entity.CorpEnum;
import com.kbfng.worktime.entity.StatEnum;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class ProjectDto {

	private Integer prjId;
	
    private String prjNm;
	
	private CorpEnum plodrCorp;
	
	private String startDt;
	
	private String endDt;
	
	private StatEnum stat;
	
	private List<EmployeeDto> empList;
	
}
