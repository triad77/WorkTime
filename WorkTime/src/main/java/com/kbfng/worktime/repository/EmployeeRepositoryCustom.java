package com.kbfng.worktime.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kbfng.worktime.entity.Employee;

public interface EmployeeRepositoryCustom {

	public Page<Employee> searchEmpByWhere(
			String empNo, String empNm, 
			String plodrCorp, int prjId, String belngCorp, Pageable pageable);
	
	public List<Employee> searchActvEmpByEmpNo(String empNo);
}
