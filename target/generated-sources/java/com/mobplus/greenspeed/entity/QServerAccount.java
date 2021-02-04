package com.mobplus.greenspeed.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QServerAccount is a Querydsl query type for ServerAccount
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QServerAccount extends EntityPathBase<ServerAccount> {

    private static final long serialVersionUID = 109900360L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QServerAccount serverAccount = new QServerAccount("serverAccount");

    public final com.mobplus.greenspeed.common.entity.QAuditTimeEntity _super = new com.mobplus.greenspeed.common.entity.QAuditTimeEntity(this);

    //inherited
    public final NumberPath<Long> createdAt = _super.createdAt;

    //inherited
    public final BooleanPath deleted = _super.deleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    //inherited
    public final NumberPath<Long> modifiedAt = _super.modifiedAt;

    public final StringPath passwd = createString("passwd");

    public final StringPath remark = createString("remark");

    public final QServer server;

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final StringPath userName = createString("userName");

    public QServerAccount(String variable) {
        this(ServerAccount.class, forVariable(variable), INITS);
    }

    public QServerAccount(Path<? extends ServerAccount> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QServerAccount(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QServerAccount(PathMetadata metadata, PathInits inits) {
        this(ServerAccount.class, metadata, inits);
    }

    public QServerAccount(Class<? extends ServerAccount> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.server = inits.isInitialized("server") ? new QServer(forProperty("server")) : null;
    }

}

