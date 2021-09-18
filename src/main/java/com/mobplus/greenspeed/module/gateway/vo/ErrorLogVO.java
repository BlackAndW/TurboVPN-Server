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
}
