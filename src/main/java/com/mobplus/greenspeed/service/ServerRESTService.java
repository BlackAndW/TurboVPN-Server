package com.mobplus.greenspeed.service;

import com.mobplus.greenspeed.entity.AppSetting;
import com.mobplus.greenspeed.entity.ErrorLog;
import com.mobplus.greenspeed.entity.Server;
import com.mobplus.greenspeed.module.gateway.form.ServerForm;
import com.mobplus.greenspeed.module.gateway.vo.SettingVO;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.Query;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2021/8/23
 */
public interface ServerRESTService {

    void create(ServerForm form) throws ServiceException;

    void update(Integer id, ServerForm form) throws ServiceException;

    void updateOnlineConn(String ipAddr, Integer onlineConn) throws ServiceException;

    void updateClose(String ipAddr) throws ServiceException;

    SettingVO getSettingByApp(String pkgName) throws ServiceException;

    void updateSettingByApp(String pkgName, Query query) throws ServiceException;

    List<Server> filterBySetting(String pkgName, List<Server> serverList) throws ServiceException;

    List<Server> sortByOrder(String pkgName, List<Server> serverList) throws ServiceException;

    Page<ErrorLog> queryErrorLog(Query query) throws ParseException;

    void createApp(Query query) throws ServiceException;
}
