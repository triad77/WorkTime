package com.kbfng.worktime.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWorkTime is a Querydsl query type for WorkTime
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorkTime extends EntityPathBase<WorkTime> {

    private static final long serialVersionUID = 1277195276L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkTime workTime = new QWorkTime("workTime");

    public final QEmployee emp;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath reason = createString("reason");

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final StringPath scanIp = createString("scanIp");

    public QWorkTime(String variable) {
        this(WorkTime.class, forVariable(variable), INITS);
    }

    public QWorkTime(Path<? extends WorkTime> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWorkTime(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWorkTime(PathMetadata metadata, PathInits inits) {
        this(WorkTime.class, metadata, inits);
    }

    public QWorkTime(Class<? extends WorkTime> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.emp = inits.isInitialized("emp") ? new QEmployee(forProperty("emp"), inits.get("emp")) : null;
    }

}

