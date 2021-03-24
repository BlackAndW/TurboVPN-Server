package com.mobplus.greenspeed.service;

import com.mobplus.greenspeed.entity.UserAdInfo;
import com.mobplus.greenspeed.module.gateway.form.UserAdInfoForm;
import com.yeecloud.meeto.common.exception.ServiceException;

/**
 * @author: Leonard
 * @create: 2021/3/23
 */
public interface UserAdInfoService {

    void createOrUpdateInfo (UserAdInfoForm form, String userIp) throws ServiceException;
}
