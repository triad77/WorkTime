package com.kbfng.worktime.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kbfng.worktime.dto.EmployeeWorkTimeDto;
import com.kbfng.worktime.entity.WorkTime;

public interface WorkTimeRepositoryCustom {

	public List<WorkTime> findByEmpNo(String empNo);
	
	public Page<WorkTime> searchWorkTimeByWhere(String empNm, String srchStYmd, String srchEdYmd, Pageable pageable); 
	
	public List<EmployeeWorkTimeDto> searchTotalEmpWorkTime(int prjId, String corpCd, String srchYmd); 
	
	public List<EmployeeWorkTimeDto> searchRegEmpWorkTime(int prjId, String corpCd, String srchYmd);
	
	public List<EmployeeWorkTimeDto> searchLateEmpWorkTime(int prjId, String corpCd, String srchYmd);
}
