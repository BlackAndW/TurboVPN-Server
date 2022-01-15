package com.mobplus.greenspeed.service;

import com.mobplus.greenspeed.module.gateway.form.ErrorLogForm;
import com.yeecloud.meeto.common.exception.ServiceException;

/**
 * @author: Leonard
 * @create: 2021/9/17
 */
public interface ErrorLogService {

    String insertErrorLog(ErrorLogForm form, String ipAddress) throws ServiceException;
}
