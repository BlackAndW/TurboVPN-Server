package com.mobplus.greenspeed.service;

import com.mobplus.greenspeed.entity.App;
import com.mobplus.greenspeed.entity.Feedback;
import com.yeecloud.meeto.common.exception.ServiceException;

/**
 * Title
 *
 * Date: 2020-09-17 10:51:11
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
public interface AppService {
    App findAppByPkgName(String pkgName) throws ServiceException;

    void createFeedback(Feedback feedback) throws ServiceException;
}
