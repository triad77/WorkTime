package com.kbfng.worktime.dto;

import com.kbfng.worktime.entity.Enum;
import com.kbfng.worktime.entity.CorpEnum;
import com.kbfng.worktime.entity.Project;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpCreateForm {
	
	private int id;
	
    @Size(min = 7, max = 7, message = "직원번호는 7자리입니다")
    @NotEmpty(message = "직원번호는 필수항목입니다.")
    private String empNo;
    
    @NotEmpty(message = "직원이름은 필수항목입니다.")
    private String empNm;
    
    @NotEmpty(message = "IP주소정보는 필수항목입니다.")
	private String ipAddr;
	
    @NotEmpty(message = "연락처정보는 필수항목입니다.")
	private String contract;
	
    @Enum(enumClass = CorpEnum.class, ignoreCase = true, message ="잘못된 (소속)회사코드입니다")
	private String belngCorp;

    @NotNull(message = "참여프로젝트는 필수항목입니다.")
	private int prjId;
	
    @NotEmpty(message = "계약시작일은 필수항목입니다.")
	private String ctrcStDate;
    
    private String ctrcEdDate;
}
