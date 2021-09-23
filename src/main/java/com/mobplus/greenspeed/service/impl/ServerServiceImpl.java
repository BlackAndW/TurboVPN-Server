package com.mobplus.greenspeed.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.google.common.collect.Lists;
import com.mobplus.greenspeed.entity.*;
import com.mobplus.greenspeed.module.gateway.form.ServerForm;
import com.mobplus.greenspeed.repository.*;
import com.mobplus.greenspeed.service.ServerRESTService;
import com.mobplus.greenspeed.service.ServerService;
import com.mobplus.greenspeed.util.IpUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    private Ip2locationRepository ip2locationRepository;
    @Autowired
    private ServerRESTService serverRESTService;

    @Override
    public List<Server> query(Query query) throws ServiceException {
        try {
            Cache normalServerCache = cacheManager.getCache("normalCache");
            Cache vipServerCache = cacheManager.getCache("normalCache");
            Cache iosNormalServerCache = cacheManager.getCache("normalCache");
            Cache iosVipServerCache = cacheManager.getCache("normalCache");

            Integer status = query.get("status", Integer.class);
            Integer type = query.get("type", Integer.class);
            Boolean clearCache = query.get("clearCache", Boolean.class);

            if (clearCache != null && clearCache) { clearCache(); }

            // 查询缓存
            List<Server> resultList = Lists.newArrayList();
            List<Server> iosResultList = Lists.newArrayList();
            String mobileOS = query.get("mobileOS", String.class);

            // 安卓和IOS缓存不同
            if (null != mobileOS && !mobileOS.equals("ios")) {
                resultList = getServerCache(normalServerCache, vipServerCache, type);
                if (resultList.size() > 1) return resultList;
            } else {
                iosResultList = getIosServerCache(iosNormalServerCache, iosVipServerCache, type);
                if (iosResultList.size() > 1) return iosResultList;
            }

            // 未缓存则查询数据库并缓存
            QServer qServer = QServer.server;
            Predicate predicate = qServer.deleted.eq(Boolean.FALSE);
            if (null != status  && status != 0 && status > 0) {
                predicate = ExpressionUtils.and(predicate, qServer.status.eq(status));
            }
            if (type != Server.Type.ALL) {
                predicate = ExpressionUtils.and(predicate, qServer.type.eq(type));
            }
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "ratio"), new Sort.Order(Sort.Direction.ASC, "name"));

            // 非ios端，去除中国节点
            if (null != mobileOS && !mobileOS.equals("ios")) {
                predicate = ExpressionUtils.and(predicate, qServer.countryCode.notEqualsIgnoreCase("CN"));
            } // ios端缓存后直接返回
            else {
                serverRepository.findAll(predicate, sort).forEach(iosResultList::add);
                if (type == Server.Type.NORMAL){
                    normalServerCache.put("iosNormalServerList", iosResultList);
                } else if (type == Server.Type.VIP){
                    vipServerCache.put("iosVipServerList", iosResultList);
                }
                return iosResultList;
            }

            serverRepository.findAll(predicate, sort).forEach(resultList::add);
            if (type == Server.Type.NORMAL){
                normalServerCache.put("normalServerList", resultList);
            } else if (type == Server.Type.VIP){
                vipServerCache.put("vipServerList", resultList);
            }
            return resultList;
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
            return null;
        }
    }

    @Override
    public ServerAccount findServerAccountByServerId(Integer serverId, Integer appId, Integer memberId, Integer deviceId, String ipAddress, String pkgNameReal) throws IOException, ServiceException {
        return executeFindServerAccountByServerId(serverId, appId, deviceId, ipAddress, pkgNameReal);
    }

    private synchronized ServerAccount executeFindServerAccountByServerId(Integer serverId, Integer appId, Integer deviceId, String ipAddress, String pkgNameReal) throws IOException, ServiceException {
        QServer qServer = QServer.server;
        Predicate predicateServer = ExpressionUtils.and(qServer.deleted.eq(Boolean.FALSE), qServer.status.eq(2));
        if (serverId != 0) {
            predicateServer = ExpressionUtils.and(predicateServer, qServer.id.eq(serverId));
        } else {
            // 新版本若参数是0，按顺序连接
            predicateServer = ExpressionUtils.and(predicateServer, qServer.type.in(Server.Type.NORMAL, Server.Type.POOL));
        }

        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "ratio"), new Sort.Order(Sort.Direction.ASC, "name"));
        PageRequest pagRequestServer = PageRequest.of(0, 99, sort);
        Page<Server> serverList = serverRepository.findAll(predicateServer, pagRequestServer);

        Server server = new Server();

        // 自动连接模式，按顺序获取节点，满载依次递进
        if (serverList.getContent().size() > 1) {
            // 根据app配置筛除节点
            List<Server> serverFilterBySetting = serverRESTService.filterBySetting(pkgNameReal, serverList.getContent());
            List<Server> serverListByOrder = serverRESTService.sortByOrder(pkgNameReal, serverFilterBySetting);
            for (Server serverItem : serverListByOrder) {
                server = serverItem;
                if (server.getOnlineConn() < server.getMaxConn()) {
                    break;
                }
            }
            // 手动连接模式，满载切换同国家其他备用节点
        } else if (serverList.getContent().size() > 0) {
            server = serverList.getContent().get(0);
            if (server.getOnlineConn() > server.getMaxConn()) {
                server = changeServer(server);
            }
        } else {
            throw new ServiceException("serverList is empty!");
        }

        if (StringUtils.isEmpty(server.getCert())) {
            server.setCert("-");
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
        String region = query.get("region", String.class);
        if (region != null && region.length() > 0) {
            predicate = ExpressionUtils.and(predicate, accountLog.country.eq(region));
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

    private List<Server> getIosServerCache(Cache iosNormalServerCache, Cache iosVipServerCache, Integer type){
        if (type == Server.Type.NORMAL) {
            if (iosNormalServerCache != null && iosNormalServerCache.get("iosNormalServerList") != null) {
                return (List<Server>) iosNormalServerCache.get("iosNormalServerList").get();
            }
        } else if (type == Server.Type.VIP) {
            if (iosVipServerCache != null && iosVipServerCache.get("iosVipServerList") != null) {
                return (List<Server>) iosVipServerCache.get("iosVipServerList").get();
            }
        }
        log.info("refresh ios cache!");
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
    synchronized void saveAccountLog(ServerAccount account, Integer appId, Integer deviceId, String ipAddress, String pkgNameReal) {
        AccountLog log = new AccountLog();
        ipAddress = "192.155.85.6";
        long ipLong = IpUtils.ipStr2long(ipAddress);
        if (ipLong != 0) {
            Ip2location ipInfo = getIpInfo(ipLong);
            if (ipInfo != null) {
                log.setCountry(ipInfo.getCountry());
                log.setRegion(ipInfo.getRegion());
                log.setCity(ipInfo.getCity());
            }
            log.setServerId(account.getServer().getId());
            log.setServerName(account.getServer().getName());
            log.setAccountId(account.getId());
            log.setAppId(appId);
            log.setPkgName(pkgNameReal);
            log.setDeviceId(deviceId);
            log.setUserIp(ipLong);
            log.setReleaseAt(0L);
            accountLogRepository.save(log);
        }
        // 统计各节点总连接数
        Server server = account.getServer();
        server.setTotalConn(server.getTotalConn() + 1);
        serverRepository.save(account.getServer());
    }

    private Ip2location getIpInfo(Long ipLong) {
        return ip2locationRepository.findIpInfo(ipLong);
    }

}
