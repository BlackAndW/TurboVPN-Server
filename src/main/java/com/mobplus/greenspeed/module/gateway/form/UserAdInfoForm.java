package com.mobplus.greenspeed.module.gateway.form;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

/**
 * @author: Leonard
 * @create: 2021/3/23
 */

@Data
public class UserAdInfoForm {

    @NotNull
    private String appId;

    @NotNull
    private String uuid;

    private String adChannel;

    private Integer adRequestCount;

    private Integer adShowCount;

    private Integer adClickCount;

}
