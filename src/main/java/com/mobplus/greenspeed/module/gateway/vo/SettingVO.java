package com.mobplus.greenspeed.module.gateway.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Title
 *
 * Date: 2020-08-06 10:37:46
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
public class SettingVO {
    @JSONField(name = "b01")
    private String shareText;
    @JSONField(name = "b02")
    private String shareTextEn;
    @JSONField(name = "b03")
    private String termsUrl;
    @JSONField(name = "b04")
    private String privacyUrl;
    @JSONField(name = "b05")
    private String facebookUrl;
    @JSONField(name = "b06")
    private String twitterUrl;
    @JSONField(name = "b07")
    private String insUrl;

    @JSONField(name = "b08")
    private long disInterval; //断开连接计时 ==> 5分钟以上有效

    //首页广告  单位: 分  0:不开广告
    @JSONField(name = "b09")
    private long adIntervalForSplash;
    //HOME页连接 单位: 分 0:不开广告
    @JSONField(name = "b10")
    private long adIntervalForConnecting;
    //HOME页    连接广告类型 1:激励视频 0:插屏
    @JSONField(name = "b12")
    private int adTypeForConnecting;
    //HOME页原生 单位: 分 0:不开广告
    @JSONField(name = "b11")
    private long adIntervalForHome;
    //FB定时器 ==> 间隔 单位: 分 0:不定时循环触发
    @JSONField(name = "b13")
    private long adIntervalForTimer;
    //FB定时器 ==> 延时 //单位: 分 <10s:关闭广告
    @JSONField(name = "b14")
    private float adIntervalForDelay;
}
