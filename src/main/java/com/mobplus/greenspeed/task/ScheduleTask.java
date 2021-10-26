package com.mobplus.greenspeed.task;

import com.mobplus.greenspeed.entity.Server;
import com.mobplus.greenspeed.module.gateway.controller.ServerController;
import com.mobplus.greenspeed.module.gateway.vo.ServerProfileVO;
import com.mobplus.greenspeed.module.gateway.vo.ServerVO;
import com.mobplus.greenspeed.util.Result;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.configure.service.ConfigureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
    private CacheManager cacheManager;

    @Autowired
    private ConfigureService configureService;

    @Autowired
    private ServerController serverController;

    @PostConstruct
    public void init() {
        this.onSchedule();
    }

    @Scheduled(fixedDelay = 60 * 1000)
    public void onSchedule() {
        //配置更新
        configureService.refresh();
    }

    /** 生成配置文件 60s更新*/
    @Scheduled(fixedDelay = 60 * 1000)
    public void getServerListCfg() throws ServiceException, IOException {
        Result<List<ServerVO>> result = serverController.getServerList("com.freetech.turbovpn", "", "android", "com.akin.cleaner.supervpn");
        String dirName = "d:/data/server/config/";
        String fileName = "com.akin.cleaner.supervpn.json";
        genConfigFile(dirName, fileName, result);
    }

    /** 生成配置文件 3s更新*/
    @Scheduled(fixedDelay = 60 * 1000)
    public void getServerProfileCgf() {
        Cache normalServerCache = cacheManager.getCache("normalCache");
        if (normalServerCache != null && normalServerCache.get("normalServerList") != null) {
            List<Server> list =  (List<Server>) normalServerCache.get("normalServerList").get();
            list.forEach(server -> {
                Result<ServerProfileVO> result = null;
                try {
                    result = serverController.getServerProfile("com.freetech.turbovpn", "", "android","","", "com.akin.cleaner.supervpn", server.getId());
                } catch (ServiceException | IOException e) {
                    e.printStackTrace();
                }
                String dirName = "d:/data/server/config/profile/" + server.getId() + "/";
                String fileName = "com.akin.cleaner.supervpn.json";
                genConfigFile(dirName, fileName, result);
            });
        }
    }

    private void genConfigFile(String dirName, String fileName, Result result) {
        File dirs = new File(dirName);
        if (!dirs.exists() && !dirs.isDirectory()) {
            dirs.mkdirs();
        }
        try(FileOutputStream os = new FileOutputStream(dirName + fileName);
            OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            writer.write(result.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}