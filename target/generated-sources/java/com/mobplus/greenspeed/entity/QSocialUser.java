package com.mobplus.greenspeed.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSocialUser is a Querydsl query type for SocialUser
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSocialUser extends EntityPathBase<SocialUser> {

    private static final long serialVersionUID = 2046735546L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSocialUser socialUser = new QSocialUser("socialUser");

    public final NumberPath<Integer> appId = createNumber("appId", Integer.class);

    public final NumberPath<Long> createdAt = createNumber("createdAt", Long.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QMember member;

    public final NumberPath<Long> modifiedAt = createNumber("modifiedAt", Long.class);

    public final StringPath pluginName = createString("pluginName");

    public final StringPath uniqueId = createString("uniqueId");

    public QSocialUser(String variable) {
        this(SocialUser.class, forVariable(variable), INITS);
    }

    public QSocialUser(Path<? extends SocialUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSocialUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSocialUser(PathMetadata metadata, PathInits inits) {
        this(SocialUser.class, metadata, inits);
    }

    public QSocialUser(Class<? extends SocialUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

