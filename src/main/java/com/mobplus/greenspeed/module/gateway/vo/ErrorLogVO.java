package com.mobplus.greenspeed.module.gateway.vo;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/9/17
 */
@Data
public class ErrorLogVO {

    /** 版本号 */
    private String pkgVersion;

    /** 错误信息 */
    private String errMsg;

    /** 创建时间 */
    private long createdAt;

    /** 用户ip */
    private String userIp;

    /** 国家 */
    private String country;

    /** 地区 */
    private String region;

    /** 城市 */
    private String city;

    /** 节点名 */
    private String serverName;
}
