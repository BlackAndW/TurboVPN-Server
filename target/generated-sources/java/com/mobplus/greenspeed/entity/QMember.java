package com.mobplus.greenspeed.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1013024636L;

    public static final QMember member = new QMember("member1");

    public final com.mobplus.greenspeed.common.entity.QBaseEntity _super = new com.mobplus.greenspeed.common.entity.QBaseEntity(this);

    public final NumberPath<Integer> appId = createNumber("appId", Integer.class);

    public final StringPath avatarUrl = createString("avatarUrl");

    public final NumberPath<Long> createdAt = createNumber("createdAt", Long.class);

    public final StringPath displayName = createString("displayName");

    public final NumberPath<Long> expireAt = createNumber("expireAt", Long.class);

    public final StringPath familyName = createString("familyName");

    public final StringPath givenName = createString("givenName");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Long> modifiedAt = createNumber("modifiedAt", Long.class);

    public final NumberPath<Integer> vipLevel = createNumber("vipLevel", Integer.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

