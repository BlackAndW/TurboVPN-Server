package com.mobplus.greenspeed.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFeedback is a Querydsl query type for Feedback
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFeedback extends EntityPathBase<Feedback> {

    private static final long serialVersionUID = -982960153L;

    public static final QFeedback feedback = new QFeedback("feedback");

    public final NumberPath<Integer> appId = createNumber("appId", Integer.class);

    public final StringPath contact = createString("contact");

    public final StringPath content = createString("content");

    public final NumberPath<Long> createdAt = createNumber("createdAt", Long.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> memberId = createNumber("memberId", Integer.class);

    public final StringPath subject = createString("subject");

    public QFeedback(String variable) {
        super(Feedback.class, forVariable(variable));
    }

    public QFeedback(Path<? extends Feedback> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFeedback(PathMetadata metadata) {
        super(Feedback.class, metadata);
    }

}

