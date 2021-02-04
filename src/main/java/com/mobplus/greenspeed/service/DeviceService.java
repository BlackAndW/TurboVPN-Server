package com.mobplus.greenspeed.service;

import com.mobplus.greenspeed.entity.Device;
import com.yeecloud.meeto.common.exception.ServiceException;

/**
 * Title
 *
 * Date: 2020-08-05 13:56:27
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
public interface DeviceService {
    void createOrUpdateDevice(Device device) throws ServiceException;

    Device findDeviceByUUID(String pkgName, String uuid) throws ServiceException;

    Device findDeviceByIMEI(String pkgName, String imei) throws ServiceException;

    Device findDeviceByIMEIOrUUID(String imei, String uuid) throws ServiceException;
}
