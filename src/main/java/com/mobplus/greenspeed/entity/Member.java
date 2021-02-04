package com.mobplus.greenspeed.entity;

import com.mobplus.greenspeed.common.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * Title
 *
 * Date: 2020-07-30 18:01:34
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
@Entity
@Table(name = "t_member")
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "n_app_id")
    private Integer appId;

    @Column(name = "c_display_name")
    private String displayName;
    @Column(name = "c_avatar_url")
    private String avatarUrl;
    @Column(name = "c_family_name")
    private String familyName;
    @Column(name = "c_given_name")
    private String givenName;

    @Column(name = "n_vip_level")
    private int vipLevel;
    @Column(name = "n_expire_at")
    private long expireAt;

    @CreatedDate
    @Column(name = "n_created_at", updatable = false)
    protected Long createdAt;
    @LastModifiedDate
    @Column(name = "n_modified_at")
    protected Long modifiedAt;
}
