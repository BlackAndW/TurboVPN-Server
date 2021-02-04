package com.mobplus.greenspeed.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QApp is a Querydsl query type for App
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QApp extends EntityPathBase<App> {

    private static final long serialVersionUID = -943425409L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QApp app = new QApp("app");

    public final com.mobplus.greenspeed.common.entity.QAuditorEntity _super = new com.mobplus.greenspeed.common.entity.QAuditorEntity(this);

    public final StringPath appName = createString("appName");

    //inherited
    public final NumberPath<Long> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Integer> createdBy = _super.createdBy;

    //inherited
    public final BooleanPath deleted = _super.deleted;

    public final StringPath desc = createString("desc");

    public final StringPath iconUrl = createString("iconUrl");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    //inherited
    public final NumberPath<Long> modifiedAt = _super.modifiedAt;

    //inherited
    public final NumberPath<Integer> modifiedBy = _super.modifiedBy;

    public final StringPath pkgName = createString("pkgName");

    public final StringPath remark = createString("remark");

    public final QAppSetting setting;

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public QApp(String variable) {
        this(App.class, forVariable(variable), INITS);
    }

    public QApp(Path<? extends App> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QApp(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QApp(PathMetadata metadata, PathInits inits) {
        this(App.class, metadata, inits);
    }

    public QApp(Class<? extends App> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.setting = inits.isInitialized("setting") ? new QAppSetting(forProperty("setting"), inits.get("setting")) : null;
    }

}

