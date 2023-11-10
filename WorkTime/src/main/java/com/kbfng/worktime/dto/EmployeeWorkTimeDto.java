package com.kbfng.worktime.dto;

import java.time.LocalDateTime;

import com.kbfng.worktime.entity.CorpEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeWorkTimeDto {

	private String prjNm;
	
	private CorpEnum corp;
	
	private String empNo;
	
	private String empNm;
	
	private LocalDateTime regTime;
	
	private String reason;
}
