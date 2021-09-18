package com.mobplus.greenspeed.module.gateway.form;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/9/17
 */
@Data
public class ErrorLogForm {

    private String pkgName;

    private String pkgVersion;

    private String errMsg;
}
