package com.kbfng.worktime.repository;

import static com.kbfng.worktime.entity.QEmployee.employee;
import static com.kbfng.worktime.entity.QProject.project;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import com.kbfng.worktime.entity.CorpEnum;
import com.kbfng.worktime.entity.Project;
import com.kbfng.worktime.entity.StatEnum;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom{

	private final JPAQueryFactory queryFactory;
	
	public List<Project> findProjectForCombo(String plodrCorp) {
		
		return queryFactory
	            .select(
	            	Projections.fields(
	            		Project.class,
	            		project.prjId,
	            		project.prjNm
	            	)
	            )
	            .from(project)
	            .where(
	            	plodrCorpEq(plodrCorp)
	            )
	            .orderBy(project.prjId.desc())
	            .fetch();
	}
	
	public Page<Project> searchProjectByWhere(String plodrCorp, int prjId, String statCd, Pageable pageable){
		
		List<Project> content = queryFactory
	            .select(
	            	Projections.fields(
	            		Project.class,
	            		project.prjId,
	            		project.prjNm,
	            		project.plodrCorp,
	            		project.startDt,
	            		project.endDt,
	            		project.stat
	            	)
	            )
	            .from(project)
	            .where(
	            	plodrCorpEq(plodrCorp),
	            	prjIdEq(prjId),
	            	statEq(statCd)
	            )
	            .orderBy(project.prjId.desc())
	            .offset(pageable.getOffset())   // 페이지 번호
	            .limit(pageable.getPageSize())  // 페이지 사이즈
	            .fetch();
	    
		JPAQuery<Long> countQuery = queryFactory
	            .select(project.count())
	            .from(project)
	            .where(
	            	plodrCorpEq(plodrCorp),
	            	prjIdEq(prjId),
	            	statEq(statCd)
	            );	    
		
		return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchOne());
	}
	
	private BooleanExpression plodrCorpEq(String plodrCorp) {
		
		if(StringUtils.hasText(plodrCorp)) {
			return plodrCorp.equals("00") ? null : project.plodrCorp.eq(CorpEnum.find(plodrCorp));
		}
		else {
			return null;
		}
	}
	
	private BooleanExpression prjIdEq(int prjId) {
		return prjId != 0 ? project.prjId.eq(prjId) : null;
	}
	
	private BooleanExpression statEq(String statCd) {
		if(StringUtils.hasText(statCd)) {
			return statCd.equals("0") ? null : project.stat.eq(StatEnum.find(statCd));
		}
		else {
			return null;
		}
	}
}
