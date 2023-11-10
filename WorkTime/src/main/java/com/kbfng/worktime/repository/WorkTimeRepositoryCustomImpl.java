package com.kbfng.worktime.repository;

import static com.kbfng.worktime.entity.QWorkTime.workTime;
import static com.kbfng.worktime.entity.QProject.project;
import static com.kbfng.worktime.entity.QEmployee.employee;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import com.kbfng.worktime.dto.EmployeeWorkTimeDto;
import com.kbfng.worktime.entity.CorpEnum;
import com.kbfng.worktime.entity.WorkTime;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WorkTimeRepositoryCustomImpl implements WorkTimeRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;
	
	@Override
	public List<WorkTime> findByEmpNo(String empNo) {
		
		List<WorkTime> content = queryFactory
				.select(
	            	Projections.fields(
	            		WorkTime.class,
	            		workTime.id
	            	)
	            )
	            .from(workTime)
				.where(
					empNoEq(empNo)
		        )
				.fetch();	
				
		return content;
	}
	
	@Override
	public Page<WorkTime> searchWorkTimeByWhere(String empNm, String srchStYmd, String srchEdYmd, Pageable pageable){
		
		List<WorkTime> content = queryFactory
	            .select(
	            	Projections.fields(
	            		WorkTime.class,
	            		workTime.id,
	            		workTime.emp,
	            		workTime.scanIp,
	            		workTime.regDate,
	            		workTime.reason
            		)
	            )
	            .from(workTime)
	            .where(
	            	 empNmLike(empNm)
	            	,isBetween(srchStYmd, srchEdYmd)
	            )
	            .orderBy(workTime.regDate.desc())
	            .offset(pageable.getOffset())   // 페이지 번호
	            .limit(pageable.getPageSize())  // 페이지 사이즈
	            .fetch();
	    
		JPAQuery<Long> countQuery = queryFactory
	            .select(workTime.count())
	            .from(workTime)
	            .where(
		            	 empNmLike(empNm)
		            	,isBetween(srchStYmd, srchEdYmd)
			    );	    
		
		return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchOne());
	}
	
	public List<EmployeeWorkTimeDto> searchTotalEmpWorkTime(int prjId, String corpCd, String srchYmd) {
	
		List<EmployeeWorkTimeDto> list = queryFactory
	            .select(
	            	Projections.fields(
	            		EmployeeWorkTimeDto.class,
	            		project.prjNm,
	            		employee.corp,
	            		employee.empNo,
	            		employee.empNm
            		)
	            )
	            .from(employee)
	            .join(employee.project, project)
	            .where(
	            	prjIdEq(prjId),
	            	corpEq(corpCd),
	            	activeEmployee()
	            )
	            .orderBy(employee.empNo.asc())
	            .fetch();
		
		return list;
	}
	
	public List<EmployeeWorkTimeDto> searchRegEmpWorkTime(int prjId, String corpCd, String srchYmd) {
		
		List<EmployeeWorkTimeDto> list = queryFactory
	            .select(
	            	Projections.fields(
	            		EmployeeWorkTimeDto.class,
	            		project.prjNm,
	            		employee.corp,
	            		employee.empNo,
	            		employee.empNm,
	            		workTime.regDate.as("regTime"),
	            		workTime.reason
            		)
	            )
	            .from(workTime)
	            .join(workTime.emp, employee)
	            .join(employee.project, project)
	            .where(
	            	prjIdEq(prjId)
	            	,corpEq(corpCd)
	            	,isBetween(srchYmd, srchYmd)
	            )
	            .orderBy(workTime.regDate.desc())
	            .fetch();
		
		return list;
	}
	
	public List<EmployeeWorkTimeDto> searchLateEmpWorkTime(int prjId, String corpCd, String srchYmd) {
		
		List<EmployeeWorkTimeDto> list = queryFactory
	            .select(
	            	Projections.fields(
	            		EmployeeWorkTimeDto.class,
	            		project.prjNm,
	            		employee.corp,
	            		employee.empNo,
	            		employee.empNm,
	            		workTime.regDate.as("regTime"),
	            		workTime.reason
            		)
	            )
	            .from(workTime)
	            .join(workTime.emp, employee)
	            .join(employee.project, project)
	            .where(
	            	prjIdEq(prjId)
	            	,corpEq(corpCd)
	            	,isLate(srchYmd)
	            )
	            .orderBy(workTime.regDate.desc())
	            .fetch();
		
		return list;
	}

	private BooleanExpression empNoEq(String empNo) {
		return StringUtils.hasText(empNo) ? workTime.emp.empNo.eq(empNo) : null;
	}
	
	private BooleanExpression empNmLike(String empNm) {
		return StringUtils.hasText(empNm) ? workTime.emp.empNm.like(empNm) : null;
	}
	
	private BooleanExpression prjIdEq(int prjId) {
		return prjId != 0 ? project.prjId.eq(prjId) : null;
	}
	
	private BooleanExpression corpEq(String corp) {
		
		if(StringUtils.hasText(corp)) {
			return corp.equals("00") ? null : employee.corp.eq(CorpEnum.find(corp));
		}
		else {
			return null;
		}
	}
	
	private BooleanExpression isBetween(String srchStYmd, String srchEdYmd) {
		
		Date srchEdDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		try {
			srchEdDate = sdf.parse(srchEdYmd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(srchEdDate);
		calendar.add(Calendar.DATE, 1);
		String tempEdYmd = sdf.format(calendar.getTime());
		
		BooleanExpression isGoeStYmd = workTime.regDate.goe(Expressions.dateTimeTemplate(LocalDateTime.class, "DATE_FORMAT( {0}, '%Y%m%d')",srchStYmd));
		// LocalDateTime으로 변환 시 00:00:00으로 변환되므로 올바르게 조회하기 위해 종료일자는 하루 뒤의 날짜로 비교한다
		BooleanExpression isLoeEdYmd = workTime.regDate.loe(Expressions.dateTimeTemplate(LocalDateTime.class, "DATE_FORMAT( {0}, '%Y%m%d')",tempEdYmd));
		
		return Expressions.allOf(isGoeStYmd, isLoeEdYmd);
	}
	
	private BooleanExpression isLate(String srchYmd) {
		
		Date srchEdDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		try {
			srchEdDate = sdf.parse(srchYmd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(srchEdDate);
		calendar.add(Calendar.DATE, 1);
		String tempEdYmd = sdf.format(calendar.getTime());
		
		// 09:05부터 24:00까지 등록건 조회  
		BooleanExpression isGoeStYmd = workTime.regDate.goe(Expressions.dateTimeTemplate(LocalDateTime.class, "DATE_FORMAT( {0}, '%Y%m%d%h%m%s')",srchYmd+"090500"));
		BooleanExpression isLoeEdYmd = workTime.regDate.loe(Expressions.dateTimeTemplate(LocalDateTime.class, "DATE_FORMAT( {0}, '%Y%m%d')",tempEdYmd));
		
		return Expressions.allOf(isGoeStYmd, isLoeEdYmd);
	}
	
	private BooleanExpression activeEmployee() {
		
		return Expressions.dateTimeTemplate(LocalDateTime.class,"DATE_FORMAT( NOW(), '%Y%m%d')").between(
    			Expressions.dateTimeTemplate(LocalDateTime.class, "DATE_FORMAT( {0}, '%Y%m%d')",employee.ctrcStDate),
    			Expressions.dateTimeTemplate(LocalDateTime.class,"DATE_FORMAT( {0}, '%Y%m%d')",employee.ctrcEdDate) );
	}
}
