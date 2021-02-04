package com.mobplus.greenspeed.module.gateway.controller;

import com.google.common.base.Joiner;
import com.mobplus.greenspeed.service.ServerStatusService;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.ParamUtils;
import com.yeecloud.meeto.common.util.StringUtils;
import com.yeecloud.meeto.configure.service.ConfigureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Title
 *
 * Date: 2020-09-18 17:40:43
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
@RestController
@RequestMapping("/mgr/api/server_status")
public class ServerStatusController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ServerStatusService serverStatusService;

    @Autowired
    private ConfigureService configureService;

    @PostMapping("/{server_id}")
    public Result update(@PathVariable("server_id") Integer serverId,
                         @RequestParam(value = "account", required = false) String[] accounts) {
        //TODO: 性结及结构优化
        String ipAddress = ParamUtils.getIpAddr(request);
        String IPList = getServerIPList();
        if (StringUtils.isNotEmpty(IPList) && !StringUtils.contains(IPList, ipAddress)) {
            log.error("SERVER_STATUS IP:[{}] now allowed!", ipAddress);
            return Result.FAILURE("ip:[" + ipAddress + "] not allowed!");
        }
        log.info("SERVER_STATUS ServerId:[{}] Online Accounts:[{}]", serverId, accounts == null ? "" : Joiner.on(",").join(accounts));
        serverStatusService.updateStatus(serverId, accounts);
        return Result.SUCCESS();
    }

    private String getServerIPList() {
        return configureService.getValueByKey("server.list", "");
    }
}
