package com.kbfng.worktime.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kbfng.worktime.entity.Project;

public interface ProjectRepositoryCustom {

	public List<Project> findProjectForCombo(String plodrCorp);
	
	public Page<Project> searchProjectByWhere(String plodrCorp, int prjId, String statCd, Pageable pageable);
}
