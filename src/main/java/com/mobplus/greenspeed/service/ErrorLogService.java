package com.mobplus.greenspeed.service;

import com.mobplus.greenspeed.module.gateway.form.ErrorLogForm;

/**
 * @author: Leonard
 * @create: 2021/9/17
 */
public interface ErrorLogService {

    String insertErrorLog(ErrorLogForm form);
}
