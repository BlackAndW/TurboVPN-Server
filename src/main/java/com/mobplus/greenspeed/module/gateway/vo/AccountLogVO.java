package com.mobplus.greenspeed.module.gateway.vo;

import lombok.Data;

/**
 * @author: Leonard
 * @create: 2021/4/30
 */
@Data
public class AccountLogVO {

    private String Ip;
    private String serverName;
    private String country;
    private String region;
    private String city;
    protected Long createdAt;
}
