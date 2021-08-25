package com.mobplus.greenspeed.entity;

import com.mobplus.greenspeed.common.entity.AuditorEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * Title
 *
 * Date: 2020-08-03 17:04:59
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
@Entity
@Table(name = "t_app_settings")
public class AppSetting extends AuditorEntity {

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "c_pkg_name")
    private String pkgName;
    @Column(name = "c_content")
    private String content;

    @Column(name = "n_order")
    private String order;

    @Column(name = "n_normal_server")
    private String normalServer;

    @Column(name = "n_vip_server")
    private String vipServer;

    @Column(name = "n_back_server")
    private String backServer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_app_id")
    private App app;
}
