package com.mobplus.greenspeed.module.gateway.convert;

import com.mobplus.greenspeed.common.convert.GeneralConvert;
import com.mobplus.greenspeed.entity.UserAdInfo;
import com.mobplus.greenspeed.module.gateway.form.UserAdInfoForm;
import org.mapstruct.Mapper;

/**
 * @author: Leonard
 * @create: 2021/3/23
 */

@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface UserAdInfoConvert {

    UserAdInfo convert(UserAdInfoForm form);

}
