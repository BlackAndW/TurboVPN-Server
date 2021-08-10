package com.mobplus.greenspeed.service;

import com.mobplus.greenspeed.entity.AccountLog;
import com.mobplus.greenspeed.entity.Feedback;
import com.mobplus.greenspeed.entity.Server;
import com.mobplus.greenspeed.entity.ServerAccount;
import com.mobplus.greenspeed.module.gateway.form.ServerForm;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Title
 *
 * Date: 2020-09-17 16:42:24
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
public interface ServerService {
    List<Server> query(Query query) throws ServiceException;

    Page<AccountLog> queryLog(Query query) throws ServiceException, ParseException;

    Server findById(Integer id) throws ServiceException;

    void create(ServerForm form) throws ServiceException;

    void update(Integer id, ServerForm form) throws ServiceException;

    void updateOnlineConn(String ipAddr, Integer onlineConn) throws ServiceException;

//    void delete(Integer[] ids) throws ServiceException;

    ServerAccount findServerAccountByServerId(Integer serverId, Integer appId, Integer memberId, Integer devId, String ipAddress, String pkgNameReal) throws ServiceException, IOException;

    List<String> getOrderByApp(String pkgName) throws ServiceException;

    void updateOrderByApp(String pkgName, Query query) throws ServiceException;

    List<Server> sortByOrder(String pkgName, List<Server> serverList) throws ServiceException;
}
