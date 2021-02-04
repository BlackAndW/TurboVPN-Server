package com.mobplus.greenspeed.common.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuditorEntity is a Querydsl query type for AuditorEntity
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAuditorEntity extends EntityPathBase<AuditorEntity> {

    private static final long serialVersionUID = -579903024L;

    public static final QAuditorEntity auditorEntity = new QAuditorEntity("auditorEntity");

    public final QAuditTimeEntity _super = new QAuditTimeEntity(this);

    //inherited
    public final NumberPath<Long> createdAt = _super.createdAt;

    public final NumberPath<Integer> createdBy = createNumber("createdBy", Integer.class);

    //inherited
    public final BooleanPath deleted = _super.deleted;

    //inherited
    public final NumberPath<Long> modifiedAt = _super.modifiedAt;

    public final NumberPath<Integer> modifiedBy = createNumber("modifiedBy", Integer.class);

    public QAuditorEntity(String variable) {
        super(AuditorEntity.class, forVariable(variable));
    }

    public QAuditorEntity(Path<? extends AuditorEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuditorEntity(PathMetadata metadata) {
        super(AuditorEntity.class, metadata);
    }

}

