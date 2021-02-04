package com.mobplus.greenspeed.module.gateway.convert;

import com.mobplus.greenspeed.common.convert.GeneralConvert;
import com.mobplus.greenspeed.entity.Device;
import com.mobplus.greenspeed.module.gateway.form.DeviceForm;
import org.mapstruct.Mapper;

/**
 * Title
 *
 * Date: 2020-07-30 22:01:02
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface DeviceConvert {
    Device convert(DeviceForm dev);
}
