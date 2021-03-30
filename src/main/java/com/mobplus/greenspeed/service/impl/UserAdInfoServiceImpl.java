package com.mobplus.greenspeed.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.mobplus.greenspeed.entity.UserAdInfo;
import com.mobplus.greenspeed.module.gateway.form.UserAdInfoForm;
import com.mobplus.greenspeed.repository.UserAdInfoRepository;
import com.mobplus.greenspeed.service.UserAdInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author: Leonard
 * @create: 2021/3/23
 */

@Service
@Slf4j
public class UserAdInfoServiceImpl implements UserAdInfoService {

    @Autowired
    UserAdInfoRepository userAdInfoRepository;

    @Async
    @Override
    public synchronized void createOrUpdateInfo(UserAdInfoForm userForm, String userIp) {
        try {
            UserAdInfo userAdInfo = userAdInfoRepository.findFirstByUuidOrderByCreatedAtDesc(userForm.getUuid());

            //判断是否需要新增一条记录
            userAdInfo = setCountValue(isNewRecord(userAdInfo, userForm), userForm, userIp);

            userAdInfoRepository.save(userAdInfo);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    private UserAdInfo isNewRecord(UserAdInfo userAdInfo, UserAdInfoForm form) {
        if (null == userAdInfo || !form.getUuid().equals(userAdInfo.getUuid()))
            return new UserAdInfo();

        //超过一天的记录新增
        if (System.currentTimeMillis() - userAdInfo.getCreatedAt() > 1000*60*60*24)
            return new UserAdInfo();

        return userAdInfo;
    }

    private UserAdInfo setCountValue(UserAdInfo userAdInfo, UserAdInfoForm userForm, String userIp) {
        NewBeanUtils.copyProperties(userAdInfo, userForm);
        userAdInfo.setUserIp(userIp);
        return userAdInfo;
    }
}
