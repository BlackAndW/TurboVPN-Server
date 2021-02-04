package com.mobplus.greenspeed.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.mobplus.greenspeed.entity.Device;
import com.mobplus.greenspeed.repository.DeviceRepository;
import com.mobplus.greenspeed.service.DeviceService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Title
 *
 * Date: 2020-08-05 14:06:12
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Async
    @Override
    public synchronized void createOrUpdateDevice(Device device) throws ServiceException {
        log.info("Create Or Update Device IMEI:[{}] UUID:[{}]", device.getImei(), device.getUuid());
        if (StringUtils.isBlank(device.getUuid())) {
            return;
        }
        try {
            Device entity = this.findDeviceByIMEIOrUUID(device.getImei(), device.getUuid());
            if (entity == null) {
                entity = new Device();
            }
            Integer id = entity.getId();
            NewBeanUtils.copyProperties(entity, device, true);
            entity.setId(id);
            this.deviceRepository.save(entity);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Device findDeviceByUUID(String pkgName, String uuid) throws ServiceException {
        return deviceRepository.findFirstByUuid(uuid);
    }

    @Override
    public Device findDeviceByIMEI(String pkgName, String imei) throws ServiceException {
        return deviceRepository.findFirstByImei(imei);
    }

    @Override
    public Device findDeviceByIMEIOrUUID(String imei, String uuid) throws ServiceException {
        if (Device.isImeiValid(imei)) {
            return deviceRepository.findFirstByImei(imei);
        } else {
            return deviceRepository.findFirstByUuid(uuid);
        }
    }
}
