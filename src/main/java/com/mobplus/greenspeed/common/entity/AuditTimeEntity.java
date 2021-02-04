package com.mobplus.greenspeed.common.entity;


import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/**
 * Title
 *
 * Date: 2019-11-06 17:24:16
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditTimeEntity extends BaseEntity {

    /**
     * 是否删除
     */
    @Column(name = "n_deleted")
    protected boolean deleted = false;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "n_created_at", updatable = false)
    protected Long createdAt;
}
