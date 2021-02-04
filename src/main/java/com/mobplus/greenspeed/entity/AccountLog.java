package com.mobplus.greenspeed.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Title
 *
 * Date: 2020-08-04 10:47:42
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "t_server_account_log")
@EntityListeners(AuditingEntityListener.class)
public class AccountLog implements Serializable {

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "n_server_id")
    private Integer serverId;
    @Column(name = "n_account_id")
    private Integer accountId;
    @Column(name = "n_app_id")
    private Integer appId;
    @Column(name = "n_member_id")
    private Integer memberId;
    @Column(name = "n_device_id")
    private Integer deviceId;
    @CreatedDate
    @Column(name = "n_created_at", updatable = false)
    protected Long createdAt;
    @Column(name = "n_release_at")
    protected Long releaseAt;
}
