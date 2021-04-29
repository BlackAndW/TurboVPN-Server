package com.mobplus.greenspeed.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.apache.commons.beanutils.NewBeanUtils;
import com.google.common.collect.Lists;
import com.mobplus.greenspeed.entity.*;
import com.mobplus.greenspeed.module.gateway.form.ServerForm;
import com.mobplus.greenspeed.repository.AccountLogRepository;
import com.mobplus.greenspeed.repository.ServerAccountRepository;
import com.mobplus.greenspeed.repository.ServerRepository;
import com.mobplus.greenspeed.service.ServerService;
import com.mobplus.greenspeed.util.IpUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Title
 *
 * Date: 2020-09-17 16:46:08
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
@Service
public class ServerServiceImpl implements ServerService {

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private ServerRepository serverRepository;
    @Autowired
    private ServerAccountRepository accountRepository;
    @Autowired
    private AccountLogRepository accountLogRepository;

    private final static String IconBaseUrl = "http://res.turbovpns.com/images/flag_";

    @Override
    public List<Server> query(Query query) throws ServiceException {
        try {
            Cache normalServerCache = cacheManager.getCache("normalCache");
            Cache vipServerCache = cacheManager.getCache("normalCache");

            Integer status = query.get("status", Integer.class);
            Integer type = query.get("type", Integer.class);
            List<Server> resultList = getServerCache(normalServerCache, vipServerCache, type);

            if (resultList .size() < 1) {
                QServer qServer = QServer.server;
                Predicate predicate = qServer.deleted.eq(Boolean.FALSE);
                if (null != status  && status != 0 && status > 0) {
                    predicate = ExpressionUtils.and(predicate, qServer.status.eq(status));
                }
                if (type != Server.Type.ALL) {
                    predicate = ExpressionUtils.and(predicate, qServer.type.eq(type));
                }
                Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "ratio"), new Sort.Order(Sort.Direction.ASC, "name"));

                serverRepository.findAll(predicate, sort).forEach(resultList::add);
                if (type == Server.Type.NORMAL){
                    normalServerCache.put("normalServerList", resultList);
                } else if (type == Server.Type.VIP){
                    vipServerCache.put("vipServerList", resultList);
                }
            }
            return resultList;
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
            return null;
        }
    }

    @Async
    @Override
    public synchronized void updateOnlineConn(String ipAddr, Integer onlineConn) throws ServiceException {
        QServer qServer = QServer.server;
        Predicate predicate = ExpressionUtils.and(qServer.deleted.eq(Boolean.FALSE), qServer.status.eq(2));
        predicate = ExpressionUtils.and(predicate, qServer.ipAddr.eq(ipAddr));
        Server server = serverRepository.findOne(predicate).orElse(null);
        if (null != server && !server.isDeleted()) {
            server.setOnlineConn(onlineConn);
            serverRepository.save(server);
        } else {
            log.info("update fail! [{}] is not exist in server list!", ipAddr);
        }
    }

    @Override
    public ServerAccount findServerAccountByServerId(Integer serverId, Integer appId, Integer memberId, Integer deviceId, String ipAddress, String pkgNameReal) throws IOException {
        return executeFindServerAccountByServerId(serverId, appId, deviceId, ipAddress, pkgNameReal);
    }

    private synchronized ServerAccount executeFindServerAccountByServerId(Integer serverId, Integer appId, Integer deviceId, String ipAddress, String pkgNameReal) throws IOException {
        QServer qServer = QServer.server;
        Predicate predicateServer = ExpressionUtils.and(qServer.deleted.eq(Boolean.FALSE), qServer.status.eq(2));
        if (serverId != 0) {
            predicateServer = ExpressionUtils.and(predicateServer, qServer.id.eq(serverId));
        } else {
            // 老版本若参数是0，会随机取一个美国服务器
            predicateServer = ExpressionUtils.and(predicateServer, qServer.type.eq(Server.Type.NORMAL));
            predicateServer = ExpressionUtils.and(predicateServer, qServer.nameEn.like("%United States%"));
        }

        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "ratio"), new Sort.Order(Sort.Direction.ASC, "name"));
        PageRequest pagRequestServer = PageRequest.of(0, 10, sort);
        Page<Server> serverList = serverRepository.findAll(predicateServer, pagRequestServer);

        Server server = new Server();
        if (serverList.getContent().size() > 0) {
            int serverIndex = new Random().nextInt(serverList.getContent().size());
            server = serverList.getContent().get(serverIndex);
            if (server.getOnlineConn() > server.getMaxConn()) {
                server = changeServer(server);
            }
            if (StringUtils.isEmpty(server.getCert())) {
                server.setCert("-");
            }
        }

        Cache accountCache = cacheManager.getCache("redisCache");
        List<ServerAccount> list = Lists.newArrayList();
        if (accountCache != null && accountCache.get("accountCache") != null) {
            list = (List<ServerAccount>)accountCache.get("accountCache").get();
        } else {
            QServerAccount qServerAccount = QServerAccount.serverAccount;
            Predicate predicate = ExpressionUtils.and(qServerAccount.deleted.eq(Boolean.FALSE), qServerAccount.status.eq(ServerAccount.State.OFFLINE));

            PageRequest pagRequest = PageRequest.of(0, 1000);
            accountRepository.findAll(predicate, pagRequest).forEach(list::add);
            accountCache.put("accountCache", list);
        }

        if (list != null && !list.isEmpty()) {
            int randomNum = new Random().nextInt(list.size());
            list.get(randomNum).setServer(server);
            ServerAccount account = list.get(randomNum);

            saveAccountLog(account, appId, deviceId, ipAddress, pkgNameReal);
            return account;
        }
        return null;
    }

    @Override
    public Server findById(Integer id) throws ServiceException {
        return null;
    }

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
    @Transactional(rollbackFor = Throwable.class)
    public Page<AccountLog> queryLog(Query query) throws ServiceException, ParseException {
        QAccountLog accountLog = QAccountLog.accountLog;
        Predicate predicate = new BooleanBuilder();
        String pkgName = query.get("pkgName", String.class);
        if (pkgName != null && pkgName.length() > 0) {
            predicate = ExpressionUtils.and(predicate, accountLog.pkgName.eq(pkgName));
        }
        String userIp = query.get("userIp", String.class);
        if (userIp != null && userIp.length() > 0) {
            predicate = ExpressionUtils.and(predicate, accountLog.userIp.eq(IpUtils.ipStr2long(userIp)));
        }
        String country = query.get("country", String.class);
        if (country != null && country.length() > 0) {
            predicate = ExpressionUtils.and(predicate, accountLog.country.eq(country));
        }
        String city = query.get("city", String.class);
        if (city != null && city.length() > 0) {
            predicate = ExpressionUtils.and(predicate, accountLog.city.eq(city));
        }
        String serverName = query.get("serverName", String.class);
        if (serverName != null && serverName.length() > 0) {
            predicate = ExpressionUtils.and(predicate, accountLog.serverName.eq(serverName));
        }
        String startTimeStr = query.get("startTimeStr", String.class);
        String endTimeStr = query.get("endTimeStr", String.class);
        if (startTimeStr != null && endTimeStr != null && startTimeStr.length() > 0 && endTimeStr.length() > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long startTime = simpleDateFormat.parse(startTimeStr).getTime();
            long endTime = simpleDateFormat.parse(endTimeStr).getTime();
            predicate = ExpressionUtils.and(predicate, accountLog.createdAt.between(startTime, endTime));
        }
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
        return accountLogRepository.findAll(predicate, pagRequest);
    }

    private List<Server> getServerCache(Cache normalServerCache, Cache vipServerCache, Integer type){
        if (type == Server.Type.NORMAL) {
            if (normalServerCache != null && normalServerCache.get("normalServerList") != null) {
                return (List<Server>) normalServerCache.get("normalServerList").get();
            }
        } else if (type == Server.Type.VIP) {
            if (vipServerCache != null && vipServerCache.get("vipServerList") != null) {
                return (List<Server>) vipServerCache.get("vipServerList").get();
            }
        }
        log.info("refresh cache!");
        return Lists.newArrayList();
    }

    private void clearCache(){
        Cache normalCache = cacheManager.getCache("normalCache");
        normalCache.clear();
        log.info("clear cache!");
    }

    private Server changeServer(Server server){
        QServer qServer = QServer.server;
        Predicate predicateServer = ExpressionUtils.and(qServer.deleted.eq(Boolean.FALSE), qServer.status.eq(2));
        predicateServer = ExpressionUtils.and(predicateServer, qServer.type.eq(Server.Type.POOL));
        predicateServer = ExpressionUtils.and(predicateServer, qServer.countryCode.eq(server.getCountryCode()));
        predicateServer = ExpressionUtils.and(predicateServer, qServer.onlineConn.lt(qServer.maxConn));

        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "ratio"), new Sort.Order(Sort.Direction.ASC, "name"));
        PageRequest pagRequestServer = PageRequest.of(0, 50, sort);
        Page<Server> serverPoolList = serverRepository.findAll(predicateServer, pagRequestServer);
        // 从满足条件的节点随机取一个，没有则返回原来的节点
        if (serverPoolList.getContent().size() > 0) {
            int serverIndex = new Random().nextInt(serverPoolList.getContent().size());
            server = serverPoolList.getContent().get(serverIndex);
            log.info("change to another server: " + server);
        }
        return server;
    }

    @Async
    synchronized void saveAccountLog(ServerAccount account, Integer appId, Integer deviceId, String ipAddress, String pkgNameReal) throws IOException {
        AccountLog log = new AccountLog();
        JSONObject data = getIpInfo(ipAddress);
        log.setServerId(account.getServer().getId());
        log.setServerName(account.getServer().getName());
        log.setAccountId(account.getId());
        log.setAppId(appId);
        log.setPkgName(pkgNameReal);
        log.setDeviceId(deviceId);
        log.setUserIp(IpUtils.ipStr2long(ipAddress));
        log.setCountry(data.getString("country"));
        log.setCity(data.getString("city"));
        log.setReleaseAt(0L);
        accountLogRepository.save(log);
    }

    private JSONObject getIpInfo(String ip) throws IOException {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("ip", ip);
        formBuilder.add("accessKey", "alibaba-inc");
        Request request = new Request.Builder()
                .url("https://ip.taobao.com/outGetIpInfo")
                .post(formBuilder.build())
                .build();
        String resultJson = client.newCall(request).execute().body().string();
        return JSONObject.parseObject(resultJson).getJSONObject("data");
    }
}
