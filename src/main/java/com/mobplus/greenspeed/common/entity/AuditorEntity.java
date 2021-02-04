package com.mobplus.greenspeed.common.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/**
 * Title
 *
 * Date: 2019-11-06 16:56:19
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditorEntity extends AuditTimeEntity {

    /** 创建人 */
    @CreatedBy
    @Column(name = "n_created_by", updatable = false)
    protected Integer createdBy;

    /** 更新人 */
    @LastModifiedBy
    @Column(name = "n_modified_by")
    protected Integer modifiedBy;
}
