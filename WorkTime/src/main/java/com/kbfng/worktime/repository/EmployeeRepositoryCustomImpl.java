package com.kbfng.worktime.repository;

import static com.kbfng.worktime.entity.QEmployee.employee;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import com.kbfng.worktime.entity.CorpEnum;
import com.kbfng.worktime.entity.Employee;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmployeeRepositoryCustomImpl implements EmployeeRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	
	@Override
	public Page<Employee> searchEmpByWhere(
			String empNo, String empNm, 
			String plodrCorp, int prjId, String belngCorp, Pageable pageable) {
		
		List<Employee> content = queryFactory
	            .select(
	            	Projections.fields(
	            		Employee.class,
	            		employee.id,
	            		employee.empNo,
	            		employee.empNm,
	            		employee.ipAddr,
	            		employee.contract,
	            		employee.corp,
	            		employee.project,
	            		employee.ctrcStDate,
	            		employee.ctrcEdDate
            		)
	            )
	            .from(employee)
	            .where(
	            	 empNoLike(empNo)
	            	,empNmLike(empNm)
	            	,prjIdEq(prjId)
	            	,corpEq(belngCorp)
	            	//,nowBetween()
	            )
	            .orderBy(employee.empNo.desc())
	            .offset(pageable.getOffset())   // 페이지 번호
	            .limit(pageable.getPageSize())  // 페이지 사이즈
	            .fetch();
	    
		JPAQuery<Long> countQuery = queryFactory
	            .select(employee.count())
	            .from(employee)
	            .where(
	            		empNoLike(empNo)
		            	,empNmLike(empNm)
		            	,prjIdEq(prjId)
		            	,corpEq(belngCorp)
		            );	    
		
		return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchOne());
	}
	
	@Override
	public List<Employee> searchActvEmpByEmpNo(String empNo) {
		
		List<Employee> content = queryFactory
	            .select(
	            	Projections.fields(
	            		Employee.class,
	            		employee.id,
	            		employee.empNo,
	            		employee.empNm,
	            		employee.ipAddr,
	            		employee.contract,
	            		employee.corp,
	            		employee.project,
	            		employee.ctrcStDate,
	            		employee.ctrcEdDate
            		)
	            )
	            .from(employee)
	            .where(
	            	 empNoLike(empNo)
	            	,nowBetween()
	            )
	            .fetch();
		
		return content;
	}
	
	private BooleanExpression empNoLike(String empNo) {
		
		return StringUtils.hasText(empNo) ? employee.empNo.like(empNo) : null;
	}
	
	private BooleanExpression empNmLike(String empNm) {
		return StringUtils.hasText(empNm) ? employee.empNm.like(empNm) : null;
	}
	
	private BooleanExpression prjIdEq(int prjId) {
		return prjId != 0 ? employee.project.prjId.eq(prjId) : null;
	}
	
	private BooleanExpression corpEq(String corp) {
		
		if(StringUtils.hasText(corp)) {
			return corp.equals("00") ? null : employee.corp.eq(CorpEnum.find(corp));
		}
		else {
			return null;
		}
	}
	
	private BooleanExpression nowBetween() {
		
		return Expressions.dateTimeTemplate(LocalDateTime.class,"DATE_FORMAT( NOW(), '%Y%m%d')").between(
    			Expressions.dateTimeTemplate(LocalDateTime.class, "DATE_FORMAT( {0}, '%Y%m%d')",employee.ctrcStDate),
    			Expressions.dateTimeTemplate(LocalDateTime.class,"DATE_FORMAT( {0}, '%Y%m%d')",employee.ctrcEdDate) );
	}

}
