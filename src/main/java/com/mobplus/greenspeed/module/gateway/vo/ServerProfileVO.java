package com.mobplus.greenspeed.module.gateway.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Title
 *
 * Date: 2020-09-17 16:14:36
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
public class ServerProfileVO {

    /** ---JSONField: b01 ---节点id */
    @JSONField(name = "b01")
    private Integer id;

    /** ---JSONField: b02 ---节点名 */
    @JSONField(name = "b02")
    private String name;

    /** ---JSONField: b03 ---域名 */
    @JSONField(name = "b03")
    private String ipAddr;

    /** ---JSONField: b04 ---用户名*/
    @JSONField(name = "b04")
    private String userName;

    /** ---JSONField: b05 ---密码 */
    @JSONField(name = "b05")
    private String passwd;

    /** ---JSONField: b06 ---证书 */
    @JSONField(name = "b06")
    private String cert;
}
