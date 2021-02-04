package com.mobplus.greenspeed.service.impl;

import com.mobplus.greenspeed.Constants;
import com.mobplus.greenspeed.entity.App;
import com.mobplus.greenspeed.entity.Feedback;
import com.mobplus.greenspeed.repository.AppRepository;
import com.mobplus.greenspeed.repository.FeedbackRepository;
import com.mobplus.greenspeed.service.AppService;
import com.yeecloud.meeto.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Title
 *
 * Date: 2020-09-17 14:03:30
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private AppRepository appRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Override
    public App findAppByPkgName(String pkgName) throws ServiceException {
        App app = appRepository.findTopByPkgName(pkgName);
        if (app != null && !app.isDeleted() && app.getStatus() == Constants.STATE_ON) {
            return app;
        }
        return null;
    }

    @Override
    public void createFeedback(Feedback feedback) throws ServiceException {
        feedbackRepository.save(feedback);
    }
}
