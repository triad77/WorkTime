package com.kbfng.worktime.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kbfng.worktime.entity.Employee;
import com.kbfng.worktime.entity.WorkTime;

@Repository
public interface WorkTimeRepository extends JpaRepository<WorkTime, Integer>, WorkTimeRepositoryCustom {

	Page<WorkTime> findAll(Pageable pageable);
	
	List<WorkTime> findByRegDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
	
	List<WorkTime> findByEmpAndRegDateBetween(Employee emp, LocalDateTime startDateTime, LocalDateTime endDateTime);
	
	@Query(value="select COUNT(1) from worktime.work_time wt join worktime.employee em on em.prj_id = :prjId and em.id = wt.emp_id where wt.reg_date >= date_format(:searchYmd,'%Y%m%d') and wt.reg_date < DATE_ADD(:searchYmd, INTERVAL 1 DAY)", nativeQuery=true)
	int getRegEmpCntAtPrj(@Param("searchYmd")String searchYmd, @Param("prjId")int prjId);
	
	@Query(value="select COUNT(1) from worktime.work_time wt join worktime.employee em on em.prj_id = :prjId and em.id = wt.emp_id where wt.reg_date >= date_format(CONCAT(:searchYmd,'090500'),'%Y-%m-%d %H:%i:%s') and wt.reg_date < DATE_ADD(:searchYmd, INTERVAL 1 DAY)", nativeQuery=true)
	int getLateEmpCntAtPrj(@Param("searchYmd")String searchYmd, @Param("prjId")int prjId);
	
	@Query(value="select COUNT(1) from worktime.work_time wt join worktime.employee em on em.corp = :corp and em.id = wt.emp_id where wt.reg_date >= date_format(:searchYmd,'%Y%m%d') and wt.reg_date < DATE_ADD(:searchYmd, INTERVAL 1 DAY)", nativeQuery=true)
	int getRegEmpCntAtCorp(@Param("searchYmd")String searchYmd, @Param("corp")String corpCd);
	
	@Query(value="select COUNT(1) from worktime.work_time wt join worktime.employee em on em.corp = :corp and em.id = wt.emp_id where wt.reg_date >= date_format(CONCAT(:searchYmd,'090500'),'%Y-%m-%d %H:%i:%s') and wt.reg_date < DATE_ADD(:searchYmd, INTERVAL 1 DAY)", nativeQuery=true)
	int getLateEmpCntAtCorp(@Param("searchYmd")String searchYmd, @Param("corp")String corpCd);
}
