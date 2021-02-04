package com.mobplus.greenspeed.service.impl;

import com.google.common.collect.Lists;
import com.mobplus.greenspeed.entity.*;
import com.mobplus.greenspeed.repository.AccountLogRepository;
import com.mobplus.greenspeed.repository.ServerAccountRepository;
import com.mobplus.greenspeed.repository.ServerRepository;
import com.mobplus.greenspeed.service.ServerService;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    @Override
    public List<Server> query(Query query) throws ServiceException {
        QServer qServer = QServer.server;

        Integer status = query.get("status", Integer.class);
        Predicate predicate = qServer.deleted.eq(Boolean.FALSE);
        if (status != 0 && status > 0) {
            predicate = ExpressionUtils.and(predicate, qServer.status.eq(status));
        }
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "ratio"));

        List<Server> resultList = Lists.newArrayList();
        serverRepository.findAll(predicate, sort).forEach(resultList::add);
        return resultList;
    }

    @Override
    public ServerAccount findServerAccountByServerId(Integer serverId, Integer appId, Integer memberId, Integer deviceId) throws ServiceException {
        ServerAccount account = executeFindServerAccountByServerId(serverId, appId, memberId, deviceId);
        if (account == null && serverId > 0) {
            account = executeFindServerAccountByServerId(0, appId, memberId, deviceId);
        }
        return account;
    }

    private synchronized ServerAccount executeFindServerAccountByServerId(Integer serverId, Integer appId, Integer memberId, Integer deviceId) throws ServiceException {
        QServerAccount qServerAccount = QServerAccount.serverAccount;
        Predicate predicate = ExpressionUtils.and(qServerAccount.deleted.eq(Boolean.FALSE), qServerAccount.status.eq(ServerAccount.State.OFFLINE));
        predicate = ExpressionUtils.and(predicate, qServerAccount.server.deleted.eq(Boolean.FALSE));
        predicate = ExpressionUtils.and(predicate, qServerAccount.server.status.eq(Server.State.RUNNING));
        if (serverId > 0) {
            predicate = ExpressionUtils.and(predicate, qServerAccount.server.id.eq(serverId));
        }

        PageRequest pagRequest = PageRequest.of(0, 1);

        Page<ServerAccount> list = accountRepository.findAll(predicate, pagRequest);
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
}
