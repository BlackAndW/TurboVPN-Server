package com.mobplus.greenspeed.common.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuditTimeEntity is a Querydsl query type for AuditTimeEntity
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAuditTimeEntity extends EntityPathBase<AuditTimeEntity> {

    private static final long serialVersionUID = 1411434714L;

    public static final QAuditTimeEntity auditTimeEntity = new QAuditTimeEntity("auditTimeEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Long> createdAt = createNumber("createdAt", Long.class);

    public final BooleanPath deleted = createBoolean("deleted");

    //inherited
    public final NumberPath<Long> modifiedAt = _super.modifiedAt;

    public QAuditTimeEntity(String variable) {
        super(AuditTimeEntity.class, forVariable(variable));
    }

    public QAuditTimeEntity(Path<? extends AuditTimeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuditTimeEntity(PathMetadata metadata) {
        super(AuditTimeEntity.class, metadata);
    }

}

