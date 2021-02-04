package com.mobplus.greenspeed.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccountLog is a Querydsl query type for AccountLog
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAccountLog extends EntityPathBase<AccountLog> {

    private static final long serialVersionUID = -1185967687L;

    public static final QAccountLog accountLog = new QAccountLog("accountLog");

    public final NumberPath<Integer> accountId = createNumber("accountId", Integer.class);

    public final NumberPath<Integer> appId = createNumber("appId", Integer.class);

    public final NumberPath<Long> createdAt = createNumber("createdAt", Long.class);

    public final NumberPath<Integer> deviceId = createNumber("deviceId", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> memberId = createNumber("memberId", Integer.class);

    public final NumberPath<Long> releaseAt = createNumber("releaseAt", Long.class);

    public final NumberPath<Integer> serverId = createNumber("serverId", Integer.class);

    public QAccountLog(String variable) {
        super(AccountLog.class, forVariable(variable));
    }

    public QAccountLog(Path<? extends AccountLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccountLog(PathMetadata metadata) {
        super(AccountLog.class, metadata);
    }

}

