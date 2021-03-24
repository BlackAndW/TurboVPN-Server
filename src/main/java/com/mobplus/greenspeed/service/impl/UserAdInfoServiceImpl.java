package com.mobplus.greenspeed.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.mobplus.greenspeed.entity.UserAdInfo;
import com.mobplus.greenspeed.module.gateway.form.UserAdInfoForm;
import com.mobplus.greenspeed.repository.UserAdInfoRepository;
import com.mobplus.greenspeed.service.UserAdInfoService;
import com.yeecloud.meeto.common.exception.ServiceException;
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
    public synchronized void createOrUpdateInfo(UserAdInfoForm form, String userIp) throws ServiceException {
        try {
            UserAdInfo userAdInfo = userAdInfoRepository.findFirstByUserIp(userIp);
            if (userAdInfo == null) {
                userAdInfo = new UserAdInfo();
            }
            NewBeanUtils.copyProperties(userAdInfo, form, true);
            userAdInfo.setUserIp(userIp);
            userAdInfoRepository.save(userAdInfo);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }
}
