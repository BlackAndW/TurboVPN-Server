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

    private static final String BASE_DIR_NAME = "d:/1-project/data/server/config/";
    private static final String FILE_NAME = "result.json";

    @PostConstruct
    public void init() {
        this.onSchedule();
    }

    @Scheduled(fixedDelay = 60 * 1000)
    public void onSchedule() {
        //配置更新
        configureService.refresh();
    }

    /** 获取服务器列表 更新/天 */
    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void getServerListCfg() throws ServiceException, IOException {
        Result<List<ServerVO>> result = serverController.getServerList("com.freetech.turbovpn", "", "android", "com.akin.cleaner.supervpn");
        String dirName = BASE_DIR_NAME + "c0001/";
        genConfigFile(dirName, result);
    }

    /** 手动连接服务器 更新/天 */
    @Scheduled(fixedDelay = 60 * 1000)
    public void getServerProfileCfg() {
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
                String dirName = BASE_DIR_NAME + "c0001/" + server.getId() + "/";
                genConfigFile(dirName, result);
            });
        }
    }

    @Scheduled(fixedDelay = 3 * 1000)
    public void getServerAutoCfg() throws IOException, ServiceException {
        Result<ServerProfileVO> result = serverController.getServerProfile("com.freetech.turbovpn", "", "android","","", "com.akin.cleaner.supervpn", 0);
        String dirName = BASE_DIR_NAME + "c0001/0/";
        genConfigFile(dirName, result);
    }

    private void genConfigFile(String dirName, Result result) {
        File dirs = new File(dirName);
        if (!dirs.exists() && !dirs.isDirectory()) {
            dirs.mkdirs();
        }
        try(FileOutputStream os = new FileOutputStream(dirName + FILE_NAME);
            OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            writer.write(result.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}