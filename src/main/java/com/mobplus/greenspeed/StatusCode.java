package com.mobplus.greenspeed;

import com.yeecloud.meeto.common.result.ResultCode;

public interface StatusCode extends ResultCode {
    ResultCode NO_AUTH = new ResultCode.DefaultResultCode(4001, "无效会话");
    ResultCode ACCOUNT_NOT_FOUND = new DefaultResultCode(5101, "账号不存在");
    ResultCode INCORRECT_PASSWD = new DefaultResultCode(5102, "密码错误");
    ResultCode ACCOUNT_DENIED = new DefaultResultCode(5103, "账号禁用");
    ResultCode ACCOUNT_LOCKED = new DefaultResultCode(5105, "账号锁定");
    ResultCode ACCOUNT_NO_AUTH = new DefaultResultCode(5106, "登录TOKEN无效");
    ResultCode RECORD_NOT_FOUND = new DefaultResultCode(5201, "记录不存在");
    ResultCode Mobile_EXISTS = new DefaultResultCode(5110, "手机号己存在");

}
