package com.kbfng.worktime.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class WorkTimeDto {

    private Integer id;
	
	private String scanIp;
	
	private LocalDateTime regDate;
	
	private EmployeeDto emp;
	
	private String reason;
	
	private String status;
	
	public void setStatus() {
		LocalDateTime baseTime =LocalDateTime.of(regDate.getYear(),regDate.getMonth(),regDate.getDayOfMonth(),9,05,00,00000);
		status = regDate.isAfter(baseTime)?"1":"0";
	}
}
