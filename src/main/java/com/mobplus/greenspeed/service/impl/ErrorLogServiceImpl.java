package com.mobplus.greenspeed.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.mobplus.greenspeed.entity.App;
import com.mobplus.greenspeed.entity.ErrorLog;
import com.mobplus.greenspeed.module.gateway.form.ErrorLogForm;
import com.mobplus.greenspeed.repository.AppRepository;
import com.mobplus.greenspeed.repository.ErrorLogRepository;
import com.mobplus.greenspeed.service.ErrorLogService;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: Leonard
 * @create: 2021/9/17
 */
@Slf4j
@Service
public class ErrorLogServiceImpl implements ErrorLogService {

    @Autowired
    AppRepository appRepository;

    @Autowired
    ErrorLogRepository errorLogRepository;

    @Override
    public String insertErrorLog(ErrorLogForm form) {
        String pkgName = form.getPkgName();
        App app = appRepository.findTopByPkgName(pkgName);
        if (app == null) {
            log.info("app is not exist!, pkgName is {}", pkgName);
            return "app is not exist!";
        }
        ErrorLog errorLog = new ErrorLog();
        NewBeanUtils.copyProperties(errorLog, form, true);
        errorLog.setApp(app);
        errorLogRepository.save(errorLog);
        return "error log has been inserted";
    }
}
