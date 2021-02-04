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
@Table(name = "t_app")
public class App extends AuditorEntity {

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "c_pkg_name")
    private String pkgName;
    @Column(name = "c_app_name")
    private String appName;
    @Column(name = "c_icon_url")
    private String iconUrl;
    @Column(name = "c_desc")
    private String desc;
    @Column(name = "n_status")
    private int status;
    @Column(name = "c_remark")
    private String remark;

    @OneToOne(mappedBy = "app", cascade = CascadeType.ALL)
    private AppSetting setting;
}
