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

    @JSONField(name = "b01")
    private Integer id;
    @JSONField(name = "b02")
    private String name;
    @JSONField(name = "b03")
    private String summary;
    @JSONField(name = "b04")
    private String iconUrl;
    @JSONField(name = "b05")
    private int speed;
}
