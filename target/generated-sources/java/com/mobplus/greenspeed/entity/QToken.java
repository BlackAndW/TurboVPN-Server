package com.mobplus.greenspeed.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QToken is a Querydsl query type for Token
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QToken extends EntityPathBase<Token> {

    private static final long serialVersionUID = -376203049L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QToken token1 = new QToken("token1");

    public final NumberPath<Integer> appId = createNumber("appId", Integer.class);

    public final NumberPath<Long> createdAt = createNumber("createdAt", Long.class);

    public final NumberPath<Long> expireAt = createNumber("expireAt", Long.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath ipAddr = createString("ipAddr");

    public final NumberPath<Long> logoutAt = createNumber("logoutAt", Long.class);

    public final QMember member;

    public final StringPath token = createString("token");

    public QToken(String variable) {
        this(Token.class, forVariable(variable), INITS);
    }

    public QToken(Path<? extends Token> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QToken(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QToken(PathMetadata metadata, PathInits inits) {
        this(Token.class, metadata, inits);
    }

    public QToken(Class<? extends Token> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

