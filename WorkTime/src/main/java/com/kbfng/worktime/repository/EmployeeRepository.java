package com.kbfng.worktime.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kbfng.worktime.entity.CorpEnum;
import com.kbfng.worktime.entity.Employee;
import com.kbfng.worktime.entity.Project;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>, EmployeeRepositoryCustom {

	Optional<Employee> findByEmpNo(String empNo);

	Page<Employee> findAll(Pageable pageable);

	@Query(value = "select COUNT(1) from worktime.employee e where e.prj_id = :prjId and e.contract_st_date <= date_format(:searchYmd,'%Y%m%d') and e.contract_ed_date >= date_format(:searchYmd,'%Y%m%d')", nativeQuery = true)
	int getActiveEmpCntAtPrj(@Param("searchYmd") String searchYmd, @Param("prjId") int prjId);

	List<Employee> findByproject(Project project);

	@Query(value = "select COUNT(1) from worktime.employee e where e.corp = :corpCd and e.contract_st_date <= date_format(:searchYmd,'%Y%m%d') and e.contract_ed_date >= date_format(:searchYmd,'%Y%m%d')", nativeQuery = true)
	int getActiveEmpCntAtCorp(@Param("searchYmd") String searchYmd, @Param("corpCd") String corpCd);

	List<Employee> findByCorp(CorpEnum corp);
}
