package com.mobplus.greenspeed.module.gateway.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author: Leonard
 * @create: 2021/2/23
 */
@Data
public class ServerForm {

    @JSONField(name = "b02")
    @NotBlank(message = "请输入节点名称")
    private String name;

    @JSONField(name = "b02_en")
    private String nameEn;

    private String summary;

    @JSONField(name = "b03")
    private String summaryEn;

//    @JSONField(name = "b05")
//    @NotNull(message = "请输入节点网速")
//    private Integer speed;

    private Integer ratio;

    private Integer maxConn;

    @NotBlank(message = "请选择国家")
    private String countryCode;

    @NotBlank(message = "请输入地区名称")
    private String region;

    @NotBlank(message = "请输入节点域名")
    private String ipAddr;

    private String schema = "1";

    private String protocol = "IKEV2";

    @NotNull(message = "请选择节点状态")
    private Integer status;

    @NotNull(message = "请选择节点类型")
    private Integer type;

}
