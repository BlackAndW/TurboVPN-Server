package com.mobplus.greenspeed.module.gateway.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Title
 *
 * Date: 2020-07-29 15:12:43
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
public class MemberForm {
    @JSONField(name = "a01")
    private String displayName;
    @JSONField(name = "a02")
    private String photoUrl;
    @JSONField(name = "a03")
    private String familyName;
    @JSONField(name = "a04")
    private String givenName;
    @JSONField(name = "a05")
    protected Integer gender;
    @JSONField(name = "a06")
    protected String birthday;
}
