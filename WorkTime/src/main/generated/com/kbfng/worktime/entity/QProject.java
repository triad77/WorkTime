package com.kbfng.worktime.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProject is a Querydsl query type for Project
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProject extends EntityPathBase<Project> {

    private static final long serialVersionUID = -1100518709L;

    public static final QProject project = new QProject("project");

    public final ListPath<Employee, QEmployee> empList = this.<Employee, QEmployee>createList("empList", Employee.class, QEmployee.class, PathInits.DIRECT2);

    public final StringPath endDt = createString("endDt");

    public final EnumPath<CorpEnum> plodrCorp = createEnum("plodrCorp", CorpEnum.class);

    public final NumberPath<Integer> prjId = createNumber("prjId", Integer.class);

    public final StringPath prjNm = createString("prjNm");

    public final StringPath startDt = createString("startDt");

    public final EnumPath<StatEnum> stat = createEnum("stat", StatEnum.class);

    public QProject(String variable) {
        super(Project.class, forVariable(variable));
    }

    public QProject(Path<? extends Project> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProject(PathMetadata metadata) {
        super(Project.class, metadata);
    }

}

