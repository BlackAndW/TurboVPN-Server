package com.mobplus.greenspeed.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.mobplus.greenspeed.entity.App;
import com.mobplus.greenspeed.entity.ErrorLog;
import com.mobplus.greenspeed.entity.Ip2location;
import com.mobplus.greenspeed.module.gateway.form.ErrorLogForm;
import com.mobplus.greenspeed.repository.AppRepository;
import com.mobplus.greenspeed.repository.ErrorLogRepository;
import com.mobplus.greenspeed.repository.Ip2locationRepository;
import com.mobplus.greenspeed.service.ErrorLogService;
import com.mobplus.greenspeed.util.IpUtils;
import com.yeecloud.meeto.common.exception.ServiceException;
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

    @Autowired
    Ip2locationRepository ip2locationRepository;

    @Override
    public String insertErrorLog(ErrorLogForm form, String ipAddress) throws ServiceException {
        String pkgName = form.getPkgName();
        App app = appRepository.findTopByPkgName(pkgName);
        if (app == null) {
            log.info("app is not exist!, pkgName is {}", pkgName);
            return "app is not exist!";
        }
        ErrorLog errorLog = new ErrorLog();
        NewBeanUtils.copyProperties(errorLog, form, true);
        errorLog.setApp(app);

        if (ipAddress == null || ipAddress.length() == 0) {
            throw new ServiceException("ipAddress is empty!");
        }
        long ipLong = IpUtils.ipStr2long(ipAddress);
        if (ipLong == 0) {
            throw new ServiceException("ip string parse to long fail!");
        }
        Ip2location ipInfo = ip2locationRepository.findIpInfo(ipLong);
        if (ipInfo == null) {
            throw new ServiceException("get ipInfo fail!");
        }
        errorLog.setUserIp(ipLong);
        errorLog.setCountry(ipInfo.getCountry());
        errorLog.setRegion(ipInfo.getRegion());
        errorLog.setCity(ipInfo.getCity());
        errorLogRepository.save(errorLog);
        return "error log has been updated";
    }
}
