package com.kbfng.worktime.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkTimeForProjectDto {

	private Integer prjId;
	
	private String prjNm;
	
	private String dateYmd;
	
	private Integer totalCnt;
	
	private Integer registCnt;
	
	private Integer lateCnt;
	
	private Integer noRegistCnt;
}
