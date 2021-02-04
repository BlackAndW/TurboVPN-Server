package com.mobplus.greenspeed.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QServer is a Querydsl query type for Server
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QServer extends EntityPathBase<Server> {

    private static final long serialVersionUID = 1184967717L;

    public static final QServer server = new QServer("server");

    public final com.mobplus.greenspeed.common.entity.QAuditorEntity _super = new com.mobplus.greenspeed.common.entity.QAuditorEntity(this);

    public final StringPath cert = createString("cert");

    public final StringPath countryCode = createString("countryCode");

    //inherited
    public final NumberPath<Long> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Integer> createdBy = _super.createdBy;

    //inherited
    public final BooleanPath deleted = _super.deleted;

    public final StringPath iconUrl = createString("iconUrl");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath ipAddr = createString("ipAddr");

    //inherited
    public final NumberPath<Long> modifiedAt = _super.modifiedAt;

    //inherited
    public final NumberPath<Integer> modifiedBy = _super.modifiedBy;

    public final StringPath name = createString("name");

    public final StringPath nameEn = createString("nameEn");

    public final StringPath protocol = createString("protocol");

    public final NumberPath<Integer> ratio = createNumber("ratio", Integer.class);

    public final StringPath region = createString("region");

    public final StringPath remark = createString("remark");

    public final StringPath schema = createString("schema");

    public final NumberPath<Integer> speed = createNumber("speed", Integer.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final StringPath summary = createString("summary");

    public final StringPath summaryEn = createString("summaryEn");

    public QServer(String variable) {
        super(Server.class, forVariable(variable));
    }

    public QServer(Path<? extends Server> path) {
        super(path.getType(), path.getMetadata());
    }

    public QServer(PathMetadata metadata) {
        super(Server.class, metadata);
    }

}

