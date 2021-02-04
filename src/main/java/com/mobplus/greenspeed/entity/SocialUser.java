package com.mobplus.greenspeed.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Title
 *
 * Date: 2020-07-30 18:06:29
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "t_social_user")
@EntityListeners(AuditingEntityListener.class)
public class SocialUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "n_app_id")
    private Integer appId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_member_id")
    private Member member;
    @Column(name = "c_plugin_name")
    private String pluginName;
    @Column(name = "c_unique_id")
    private String uniqueId;
    @CreatedDate
    @Column(name = "n_created_at", updatable = false)
    protected Long createdAt;
    @LastModifiedDate
    @Column(name = "n_modified_at")
    protected Long modifiedAt;
}
