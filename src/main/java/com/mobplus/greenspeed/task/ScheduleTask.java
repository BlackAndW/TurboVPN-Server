package com.mobplus.greenspeed.task;

import com.yeecloud.meeto.configure.service.ConfigureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Title
 * Description:
 * Date: 2019/3/14 15:44
 * Copyright (c) 2019-2099 YYSKYS YeeCloud
 *
 * @author ybbk
 */
@Slf4j
@Component
public class ScheduleTask {

    @Autowired
    private ConfigureService configureService;

    @PostConstruct
    public void init() {
        this.onSchedule();
    }

    @Scheduled(fixedDelay = 60 * 1000)
    public void onSchedule() {
        //配置更新
        configureService.refresh();
    }
}