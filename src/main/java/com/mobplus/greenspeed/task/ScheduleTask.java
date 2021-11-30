package com.mobplus.greenspeed.task;

import com.mobplus.greenspeed.entity.QServer;
import com.mobplus.greenspeed.entity.Server;
import com.mobplus.greenspeed.module.gateway.controller.ServerController;
import com.mobplus.greenspeed.module.gateway.vo.ServerProfileVO;
import com.mobplus.greenspeed.module.gateway.vo.ServerVO;
import com.mobplus.greenspeed.repository.ServerRepository;
import com.mobplus.greenspeed.util.Result;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
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
    private ServerRepository serverRepository;

    @Autowired
    private ConfigureService configureService;

    @Autowired
    private ServerController serverController;

    private static final String BASE_DIR_NAME = "/www/wwwroot/cdn/";

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

    /** 获取普通服务器列表 更新/天 */
//    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void getServerListCfg() throws ServiceException, IOException {
        Result result = serverController.getServerList("com.freetech.turbovpn", "", "android", "com.freetech.turbovpn", "1.1");
        String dirName = BASE_DIR_NAME + "/app/api/v1/c03/c0001/";
        genConfigFile(dirName, result);
    }

    /** 获取VIP服务器列表 更新/天 */
//    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void getVIPServerListCfg() throws ServiceException, IOException {
        Result result = serverController.getServerList("com.freetech.turbovpn", "", "android", "com.freetech.turbovpn", "1.1");
        String dirName = BASE_DIR_NAME + "/app/api/v1/c03/vip/";
        genConfigFile(dirName, result);
    }

    /** 手动连接服务器 更新/天 */
//    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void getServerProfileCfg() {
        QServer Qserver = QServer.server;
        Predicate predicate = Qserver.deleted.eq(Boolean.FALSE);
        predicate = ExpressionUtils.and(predicate, Qserver.status.eq(Server.State.RUNNING));
        List<Server> list =  (List<Server>)serverRepository.findAll(predicate);
        list.forEach(server -> {
            Result result = null;
            try {
                result = serverController.getServerProfile("com.freetech.turbovpn", "", "android","","", "com.freetech.turbovpn", "1.1", server.getId());
            } catch (ServiceException | IOException e) {
                e.printStackTrace();
            }
            String dirName = BASE_DIR_NAME + "/app/api/v1/c03/c0001/" + server.getId() + "/";
            genConfigFile(dirName, result);
        });
    }

//    @Scheduled(fixedDelay = 3 * 1000)
    public void getServerAutoCfg() throws IOException, ServiceException {
        Result result = serverController.getServerProfile("com.freetech.turbovpn", "", "android","","", "com.freetech.turbovpn", "1.1", 0);
        String dirName = BASE_DIR_NAME + "/app/api/v1/c03/c0001/0/";
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