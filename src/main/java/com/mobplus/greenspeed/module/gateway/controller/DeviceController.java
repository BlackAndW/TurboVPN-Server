package com.mobplus.greenspeed.module.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.mobplus.greenspeed.entity.App;
import com.mobplus.greenspeed.entity.AppSetting;
import com.mobplus.greenspeed.entity.Device;
import com.mobplus.greenspeed.module.gateway.convert.DeviceConvert;
import com.mobplus.greenspeed.module.gateway.form.DeviceForm;
import com.mobplus.greenspeed.module.gateway.vo.SettingVO;
import com.mobplus.greenspeed.service.AppService;
import com.mobplus.greenspeed.service.DeviceService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Title
 *
 * Date: 2020-08-04 17:57:51
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
@RestController
@RequestMapping("/app/api/v1/c01")
public class DeviceController {

    @Autowired
    private DeviceConvert convert;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AppService appService;

    @PostMapping("/c0001")
    public Result getSetting(@RequestBody DeviceForm form) throws ServiceException {
        Device device = convert.convert(form);
        deviceService.createOrUpdateDevice(device);

        App app = appService.findAppByPkgName(form.getPkgName());
        if (app != null) {
            AppSetting setting = app.getSetting();
            if (setting != null) {
                SettingVO vo = JSON.parseObject(setting.getContent(), SettingVO.class);
                log.info("RESP: {}", JSON.toJSONString(vo));
                return Result.SUCCESS(vo);
            }
        }
        return Result.FAILURE("app:[" + form.getPkgName() + "] not exists!");
    }
}
