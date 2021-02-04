package com.mobplus.greenspeed.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAppSetting is a Querydsl query type for AppSetting
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAppSetting extends EntityPathBase<AppSetting> {

    private static final long serialVersionUID = -1559138095L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAppSetting appSetting = new QAppSetting("appSetting");

    public final com.mobplus.greenspeed.common.entity.QAuditorEntity _super = new com.mobplus.greenspeed.common.entity.QAuditorEntity(this);

    public final QApp app;

    public final StringPath content = createString("content");

    //inherited
    public final NumberPath<Long> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Integer> createdBy = _super.createdBy;

    //inherited
    public final BooleanPath deleted = _super.deleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    //inherited
    public final NumberPath<Long> modifiedAt = _super.modifiedAt;

    //inherited
    public final NumberPath<Integer> modifiedBy = _super.modifiedBy;

    public final StringPath pkgName = createString("pkgName");

    public QAppSetting(String variable) {
        this(AppSetting.class, forVariable(variable), INITS);
    }

    public QAppSetting(Path<? extends AppSetting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAppSetting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAppSetting(PathMetadata metadata, PathInits inits) {
        this(AppSetting.class, metadata, inits);
    }

    public QAppSetting(Class<? extends AppSetting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.app = inits.isInitialized("app") ? new QApp(forProperty("app"), inits.get("app")) : null;
    }

}

