package com.mobplus.greenspeed.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Title
 *
 * Date: 2020-09-17 11:11:14
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "t_token")
public class Token implements Serializable {

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "c_token")
    private String token;
    @Column(name = "n_app_id")
    private Integer appId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_member_id")
    private Member member;
    @Column(name = "c_ip_addr")
    private String ipAddr;

    @Column(name = "n_created_at", updatable = false)
    protected Long createdAt;
    @Column(name = "n_expire_at")
    protected Long expireAt;
    @Column(name = "n_logout_at")
    protected Long logoutAt;
}
