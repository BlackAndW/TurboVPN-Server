package com.mobplus.greenspeed.module.gateway.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Title
 *
 * Date: 2020-08-04 18:34:28
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
public class DeviceForm {

    @JSONField(name = "a01")
    private String appName;
    @JSONField(name = "a02")
    private String pkgName;
    @JSONField(name = "a03")
    private String pkgVersion;
    @JSONField(name = "a04")
    private String uuid;
    @JSONField(name = "a05")
    private String os;
    @JSONField(name = "a06")
    private String osv;
    @JSONField(name = "a07")
    private Integer devType;
    @JSONField(name = "a08")
    private String devBrand;
    @JSONField(name = "a09")
    private String devModel;
    @JSONField(name = "a10")
    private String devMake;
    @JSONField(name = "a11")
    private String fingerPrint;
    @JSONField(name = "a12")
    private String androidId;
    @JSONField(name = "a13")
    private String gaid;
    @JSONField(name = "a14")
    private String imei;
    @JSONField(name = "a15")
    private String imsi;
    @JSONField(name = "a16")
    private String iccid;
    @JSONField(name = "a17")
    private String screen;
    @JSONField(name = "a18")
    private Integer orien;
    @JSONField(name = "a19")
    private String bssid;
    @JSONField(name = "a20")
    private String mac;
    @JSONField(name = "a21")
    private Integer network;
    @JSONField(name = "a22")
    private String networkExtra;
    @JSONField(name = "a23")
    private String lac;
    @JSONField(name = "a24")
    private Double gpsLat;
    @JSONField(name = "a25")
    private Double gpsLng;
    @JSONField(name = "a26")
    private Long gpsTimestamp;
}
