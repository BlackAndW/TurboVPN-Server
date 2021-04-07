package com.mobplus.greenspeed.entity;

import com.mobplus.greenspeed.common.entity.AuditorEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * Title
 *
 * Date: 2020-08-03 16:56:28
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
@Entity
@Table(name = "t_server")
public class Server extends AuditorEntity {

    public static interface State {
        int SUSPEND = 1;
        int RUNNING = 2;
    }

    public static interface Type {
        int NORMAL = 0;
        int VIP = 1;
    }

    @Id
    @Column(name = "n_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "c_name")
    private String name;
    @Column(name = "c_name_en")
    private String nameEn;
    @Column(name = "c_summary")
    private String summary;
    @Column(name = "c_summary_en")
    private String summaryEn;
    @Column(name = "c_icon_url")
    private String iconUrl;
    @Column(name = "n_speed")
    private int speed;
    @Column(name = "n_ratio")
    private int ratio;
    @Column(name = "c_country_code")
    private String countryCode;
    @Column(name = "c_region")
    private String region;
    @Column(name = "c_ip_addr")
    private String ipAddr;
    @Column(name = "c_schema")
    private String schema;
    @Column(name = "c_protocol")
    private String protocol;
    @Column(name = "c_cert")
    private String cert;
    @Column(name = "n_type")
    private Integer type;
    @Column(name = "n_status")
    private int status;
    @Column(name = "c_remark")
    private String remark;
}
