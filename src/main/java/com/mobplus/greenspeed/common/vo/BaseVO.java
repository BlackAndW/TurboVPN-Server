package com.mobplus.greenspeed.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Title
 *
 * Date: 2019-11-09 02:01:42
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Data
public abstract class BaseVO implements Serializable {

    @ApiModelProperty("ID")
    protected Integer id;

    @ApiModelProperty("添加人")
    protected Integer createdBy;

    @ApiModelProperty("添加人名字")
    protected String createdName;

    @ApiModelProperty("添加时间")
    protected Long createdAt;

    @ApiModelProperty("修改人")
    private Integer modifiedBy;

    @ApiModelProperty("修改人名字")
    protected String modifiedName;

    @ApiModelProperty("修改时间")
    protected Long modifiedAt;
}
