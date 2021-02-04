package com.mobplus.greenspeed.module.gateway.form;

import io.swagger.annotations.ApiModelProperty;
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
public class LoginForm {
    @ApiModelProperty(name = "用户名", required = true)
    private String loginName;
    @ApiModelProperty(name = "密码", required = true)
    private String passwd;
}
