package com.mobplus.greenspeed.service;

/**
 * Title
 *
 * Date: 2020-09-18 17:43:52
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
public interface ServerStatusService {
    void updateStatus(Integer serverId, String[] accounts);
}
