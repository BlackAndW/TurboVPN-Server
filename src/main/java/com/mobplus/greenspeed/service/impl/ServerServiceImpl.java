package com.mobplus.greenspeed.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.google.common.collect.Lists;
import com.mobplus.greenspeed.entity.*;
import com.mobplus.greenspeed.module.gateway.form.ServerForm;
import com.mobplus.greenspeed.repository.AccountLogRepository;
import com.mobplus.greenspeed.repository.ServerAccountRepository;
import com.mobplus.greenspeed.repository.ServerRepository;
import com.mobplus.greenspeed.service.ServerService;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private ServerRepository serverRepository;
    @Autowired
    private ServerAccountRepository accountRepository;
    @Autowired
    private AccountLogRepository accountLogRepository;

    private final static String IconBaseUrl = "http://res.turbovpns.com/images/flag_";

    @Override
    public List<Server> query(Query query) throws ServiceException {
        QServer qServer = QServer.server;

        Integer status = query.get("status", Integer.class);
        Predicate predicate = qServer.deleted.eq(Boolean.FALSE);
        if (null != status && status != 0 && status > 0) {
            predicate = ExpressionUtils.and(predicate, qServer.status.eq(status));
        }
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "ratio"));

        List<Server> resultList = Lists.newArrayList();
        serverRepository.findAll(predicate, sort).forEach(resultList::add);
        return resultList;
    }

    @Override
    public ServerAccount findServerAccountByServerId(Integer serverId, Integer appId, Integer memberId, Integer deviceId) throws ServiceException {
        return executeFindServerAccountByServerId(serverId, appId, memberId, deviceId);
    }

    private synchronized ServerAccount executeFindServerAccountByServerId(Integer serverId, Integer appId, Integer memberId, Integer deviceId) throws ServiceException {
        QServer qServer = QServer.server;
        Predicate predicateServer = ExpressionUtils.and(qServer.deleted.eq(Boolean.FALSE), qServer.status.eq(2));
        predicateServer = ExpressionUtils.and(predicateServer, qServer.id.eq(serverId));

        QServerAccount qServerAccount = QServerAccount.serverAccount;
        Predicate predicate = ExpressionUtils.and(qServerAccount.deleted.eq(Boolean.FALSE), qServerAccount.status.eq(ServerAccount.State.OFFLINE));
        predicate = ExpressionUtils.and(predicate, qServerAccount.server.deleted.eq(Boolean.FALSE));
        predicate = ExpressionUtils.and(predicate, qServerAccount.server.status.eq(Server.State.RUNNING));

        PageRequest pagRequest = PageRequest.of(0, 1);
        Server server = serverRepository.findAll(predicateServer, pagRequest).getContent().get(0);
        if (StringUtils.isEmpty(server.getCert())) {
            server.setCert("-");
        }
        Page<ServerAccount> list = accountRepository.findAll(predicate, pagRequest);
        list.getContent().get(0).setServer(server);
        if (!list.getContent().isEmpty()) {
            ServerAccount account = list.getContent().get(0);
            account.setStatus(ServerAccount.State.PENDING);
            accountRepository.save(account);

            AccountLog log = new AccountLog();
            log.setServerId(account.getServer().getId());
            log.setAccountId(account.getId());
            log.setAppId(appId);
            log.setMemberId(memberId);
            log.setDeviceId(deviceId);
            log.setReleaseAt(0L);
            accountLogRepository.save(log);

            return account;
        }
        return null;
    }

    @Override
    public Server findById(Integer id) throws ServiceException {
        try{
            Server server = serverRepository.findById(id).orElse(null);
            if (null != server && !server.isDeleted()) {
                return server;
            } else {
                return null;
            }
        } catch (Throwable e){
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(ServerForm form) throws ServiceException {
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

//    @Override
//    @Transactional(rollbackFor = Throwable.class)
//    public void delete(Integer[] ids) throws ServiceException {
//        serverRepository.deleteById(ids);
//    }
}
