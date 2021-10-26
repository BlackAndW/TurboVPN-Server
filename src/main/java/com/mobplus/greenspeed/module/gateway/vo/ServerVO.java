package com.mobplus.greenspeed.module.gateway.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Title
 *
 * Date: 2020-09-17 16:14:10
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
public class ServerVO {

    /** ---JSONField: b01 ---节点id */
    @JSONField(name = "b01")
    private Integer id;

    /** JSONField: b02 ---节点名 */
    @JSONField(name = "b02")
    private String name;

    /** ---JSONField: b03 ---地区（英文） */
    @JSONField(name = "b03")
    private String summary;

    /** ---JSONField: b04 ---flag url */
    @JSONField(name = "b04")
    private String iconUrl;

    /** ---JSONField: b05 ---speed */
    @JSONField(name = "b05")
    private Integer speed;

    /** ---优先级 */
    private Integer ratio;

    /** ---节点中文名 */
    @JSONField(name = "serverName")
    private String serverName;

    /** ---国家简称 */
    @JSONField(name = "countryCode")
    private String countryCode;

    /** ---地区中文名 */
    private String region;

    /** ---域名 */
    @JSONField(name = "ipAddr")
    private String ipAddr;

    /** ---状态 */
    private Integer status;

    /** ---类型 */
    private Integer type;

    /** ---在线连接数 */
    @JSONField(name = "onlineConn")
    private Integer onlineConn;

    /** ---最大连接数 */
    @JSONField(name = "maxConn")
    private Integer maxConn;

    /** ---总连接数 */
    @JSONField(name = "totalConn")
    private Long totalConn;

}
