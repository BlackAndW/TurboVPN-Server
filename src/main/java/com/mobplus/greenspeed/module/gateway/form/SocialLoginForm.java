package com.mobplus.greenspeed.module.gateway.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Title
 *
 * Date: 2020-07-29 15:18:35
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
public class SocialLoginForm {
    @JSONField(name = "a01")
    private String uniqueId;
    @JSONField(name = "a02")
    private String type = "dev";
}
