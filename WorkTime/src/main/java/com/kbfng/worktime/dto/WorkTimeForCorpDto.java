package com.kbfng.worktime.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkTimeForCorpDto {

	private String corpCd;
	
	private String corpNm;
	
	private String dateYmd;
	
	private Integer totalCnt;
	
	private Integer registCnt;
	
	private Integer lateCnt;
	
	private Integer noRegistCnt;
}
