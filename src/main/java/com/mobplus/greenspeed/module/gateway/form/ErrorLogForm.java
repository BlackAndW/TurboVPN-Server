package com.mobplus.greenspeed.module.gateway.form;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/9/17
 */
@Data
public class ErrorLogForm {

    /** 真实包名 */
    private String pkgName;

    /** 版本号 */
    private String pkgVersion;

    /** 错误信息 */
    private String errMsg;

    /** 访问节点名 */
    private String serverName;

    /** 手机系统版本 */
    private String os;
}
