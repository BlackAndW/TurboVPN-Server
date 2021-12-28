package com.mobplus.greenspeed.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.mobplus.greenspeed.entity.*;
import com.mobplus.greenspeed.module.gateway.form.ServerForm;
import com.mobplus.greenspeed.module.gateway.vo.SettingVO;
import com.mobplus.greenspeed.repository.AppRepository;
import com.mobplus.greenspeed.repository.ErrorLogRepository;
import com.mobplus.greenspeed.repository.ServerRepository;
import com.mobplus.greenspeed.service.ServerRESTService;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/8/23
 */

@Slf4j
@Service
public class ServerRESTServiceImpl implements ServerRESTService {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private ErrorLogRepository errorLogRepository;

    private final static String IconBaseUrl = "https://res.turbovpns.com/images/flag_";

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(ServerForm form) throws ServiceException {
        clearCache();
        Server server = new Server();
        NewBeanUtils.copyProperties(server, form, true);
        server.setIconUrl(IconBaseUrl + form.getCountryCode().toLowerCase() + ".png");
        try {
            serverRepository.save(server);
        } catch (Throwable e){
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Integer id, ServerForm form) throws ServiceException {
        try {
            clearCache();
            Server server = serverRepository.findById(id).orElse(null);
            if (null != server && !server.isDeleted()) {
                NewBeanUtils.copyProperties(server, form, true);
                server.setIconUrl(IconBaseUrl + form.getCountryCode().toLowerCase() + ".png");
                serverRepository.save(server);
            }
        } catch (Throwable e){
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateOnlineConn(String ipAddr, Integer onlineConn) throws ServiceException {
        QServer qServer = QServer.server;
        Predicate predicate = ExpressionUtils.and(qServer.deleted.eq(Boolean.FALSE), qServer.status.eq(Server.State.RUNNING));
        predicate = ExpressionUtils.and(predicate, qServer.ipAddr.eq(ipAddr));
        Server server = serverRepository.findOne(predicate).orElse(null);
        if (server != null) {
            server.setOnlineConn(onlineConn);
            serverRepository.save(server);
        } else {
            log.info("update fail! [{}] is not exist in server list!", ipAddr);
        }
    }

    @Override
    public void updateClose(String ipAddr) throws ServiceException {
        QServer qServer = QServer.server;
        Predicate predicate = ExpressionUtils.and(qServer.deleted.eq(Boolean.FALSE), qServer.status.eq(Server.State.RUNNING));
        predicate = ExpressionUtils.and(predicate, qServer.ipAddr.eq(ipAddr));
        Server server = serverRepository.findOne(predicate).orElse(null);
        if (server != null) {
            server.setStatus(Server.State.SUSPEND);
            serverRepository.save(server);
            log.info("[{}] has been stopped!!!", ipAddr);
        }
    }

    @Override
    public SettingVO getSettingByApp(String pkgName) throws ServiceException {
        App app = getAppByPkgName(pkgName);
        SettingVO vo = new SettingVO();
        String order = app.getSetting().getOrder();
        if (order != null && order.length() > 0) {
            vo.setOrder(Arrays.asList(app.getSetting().getOrder().split("-")));
        }
        String normalServer = app.getSetting().getNormalServer();
        if (normalServer != null && normalServer.length() > 0) {
            vo.setNormalServerList(Arrays.stream(normalServer.split("-")).mapToInt(Integer::parseInt).toArray());
        }
        String vipServer = app.getSetting().getVipServer();
        if (vipServer != null && vipServer.length() > 0) {
            vo.setVipServerList(Arrays.stream(vipServer.split("-")).mapToInt(Integer::parseInt).toArray());
        }
        String backServer = app.getSetting().getBackServer();
        if (backServer != null && backServer.length() > 0) {
            vo.setBackServerList(Arrays.stream(backServer.split("-")).mapToInt(Integer::parseInt).toArray());
        }
        return vo;
    }

    /***
     * 保存app自定义setting
     * @param pkgName
     * @param query
     * @throws ServiceException
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateSettingByApp(String pkgName, Query query) throws ServiceException {
        App app = getAppByPkgName(pkgName);

        // 排序列表
        String[] order = query.get("order", String[].class);
        if (order != null) {
            if (order.length == 0) {
                app.getSetting().setOrder("");
                appRepository.save(app);
            } else {
                String orderStr = Arrays.toString(order);
                orderStr = orderStr.substring(1, orderStr.length() - 1);
                orderStr = orderStr.replaceAll(", ", "-");
                app.getSetting().setOrder(orderStr);
                appRepository.save(app);
            }
        }

        // 普通节点列表
        String[] serverNM = query.get("serverNMCheckList", String[].class);
        if (serverNM != null) {
            if (serverNM.length == 0) {
                app.getSetting().setNormalServer("");
                appRepository.save(app);
            } else {
                String serverNMStr = Arrays.toString(serverNM);
                serverNMStr = serverNMStr.substring(1, serverNMStr.length() - 1);
                serverNMStr = serverNMStr.replaceAll(", ", "-");
                app.getSetting().setNormalServer(serverNMStr);
                appRepository.save(app);
            }
        }

        // VIP节点列表
        String[] serverVIP = query.get("serverVIPCheckList", String[].class);
        if (serverVIP != null) {
            if (serverVIP.length == 0) {
                app.getSetting().setVipServer("");
                appRepository.save(app);
            } else {
                String serverVIPStr = Arrays.toString(serverVIP);
                serverVIPStr = serverVIPStr.substring(1, serverVIPStr.length() - 1);
                serverVIPStr = serverVIPStr.replaceAll(", ", "-");
                app.getSetting().setVipServer(serverVIPStr);
                appRepository.save(app);
            }
        }

        // 备用节点列表
        String[] serverBK = query.get("serverBKCheckList", String[].class);
        if (serverBK != null) {
            if (serverBK.length == 0) {
                app.getSetting().setBackServer("");
                appRepository.save(app);
            } else {
                String serverBKStr = Arrays.toString(serverBK);
                serverBKStr = serverBKStr.substring(1, serverBKStr.length() - 1);
                serverBKStr = serverBKStr.replaceAll(", ", "-");
                app.getSetting().setBackServer(serverBKStr);
                appRepository.save(app);
            }
        }
    }

    /***
     * 筛除配置中没有的节点
     * @param pkgName
     * @param serverList
     * @return
     * @throws ServiceException
     */
    @Override
    public List<Server> filterBySetting(String pkgName, List<Server> serverList) throws ServiceException {
        SettingVO vo = getSettingByApp(pkgName);
        int[] settingServer = ArrayUtils.addAll(
                ArrayUtils.addAll(  vo.getNormalServerList(),
                                    vo.getVipServerList()),
                vo.getBackServerList());
        if (settingServer != null && settingServer.length > 0) {
            List<Server> servers = new ArrayList<>();
            for (Server server : serverList) {
                if (ArrayUtils.contains(settingServer, server.getId())) {
                    servers.add(server);
                }
            }
            return servers;
        }
        return serverList;
    }

    /***
     * 将列表按app内配置的国家顺序排序
     * @param pkgName
     * @param serverList
     * @return
     * @throws ServiceException
     */
    @Override
    public List<Server> sortByOrder(String pkgName, List<Server> serverList) throws ServiceException {
        App app = getAppByPkgName(pkgName);
        if (app.getSetting().getOrder() == null || app.getSetting().getOrder().length() == 0) {
            log.info("order config is empty");
            return serverList;
        }
        List<String> orders = Arrays.asList(app.getSetting().getOrder().split("-"));
        List<Server> newServerList = new ArrayList<>();
        for (String order : orders) {
            for (Server server : serverList) {
                if (server.getCountryCode().equals(order)) {
                    newServerList.add(server);
                }
            }
        }
        // 未排序的部分按默认排序添加
        if (serverList.size() > newServerList.size()) {
            for (Server server : serverList) {
                if (!orders.contains(server.getCountryCode())) {
                    newServerList.add(server);
                }
            }
        }
        return newServerList;
    }

    private void clearCache(){
        Cache normalCache = cacheManager.getCache("normalCache");
        normalCache.clear();
        log.info("clear cache!");
    }

    private App getAppByPkgName(String pkgName) throws ServiceException{
        App app = appRepository.findTopByPkgName(pkgName);
        if (app == null) {
            throw new ServiceException(pkgName + " is not exist!");
        }
        return app;
    }

    @Override
    public Page<ErrorLog> queryErrorLog(Query query) throws ParseException {
        QErrorLog qErrorLog = QErrorLog.errorLog;
        Predicate predicate = qErrorLog.deleted.eq(false);
        String pkgName = query.get("pkgName", String.class);
        App app = appRepository.findTopByPkgName(pkgName);
        if (app == null) {
            log.info("app is not exist!, pkgName is {}", pkgName);
            return null;
        }
        predicate = ExpressionUtils.and(predicate, qErrorLog.app.id.eq(app.getId()));
        String startTimeStr = query.get("startTimeStr", String.class);
        String endTimeStr = query.get("endTimeStr", String.class);
        if (startTimeStr != null && endTimeStr != null && startTimeStr.length() > 0 && endTimeStr.length() > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long startTime = simpleDateFormat.parse(startTimeStr).getTime();
            long endTime = simpleDateFormat.parse(endTimeStr).getTime();
            predicate = ExpressionUtils.and(predicate, qErrorLog.createdAt.between(startTime, endTime));
        }
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
        return errorLogRepository.findAll(predicate, pagRequest);
    }

    @Override
    public void createApp(Query query) throws ServiceException {
        String appName = query.get("name", String.class);
        String pkgName = query.get("pkgName", String.class);
        App app = new App();
        if (appName != null && appName.length() > 0 && pkgName != null && pkgName.length() > 0) {
            app.setAppName(appName);
            app.setPkgName(pkgName);
            app.setStatus(0);
            AppSetting appSetting = new AppSetting();
            appSetting.setApp(app);
            appSetting.setPkgName(pkgName);
            app.setSetting(appSetting);
            appRepository.save(app);
        }
    }
}
