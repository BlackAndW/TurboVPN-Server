package com.mobplus.greenspeed.service;

import com.mobplus.greenspeed.module.gateway.form.UserAdInfoForm;

/**
 * @author: Leonard
 * @create: 2021/3/23
 */
public interface UserAdInfoService {

    void createOrUpdateInfo (UserAdInfoForm userForm, String userIp);
}
