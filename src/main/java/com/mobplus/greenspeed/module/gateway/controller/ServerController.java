package com.mobplus.greenspeed.module.gateway.controller;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mobplus.greenspeed.Constants;
import com.mobplus.greenspeed.entity.*;
import com.mobplus.greenspeed.module.gateway.convert.ServerConvert;
import com.mobplus.greenspeed.module.gateway.vo.ServerProfileVO;
import com.mobplus.greenspeed.module.gateway.vo.ServerVO;
import com.mobplus.greenspeed.service.AppService;
import com.mobplus.greenspeed.service.DeviceService;
import com.mobplus.greenspeed.service.MemberService;
import com.mobplus.greenspeed.service.ServerService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.result.ResultCode;
import com.yeecloud.meeto.common.util.ParamUtils;
import com.yeecloud.meeto.common.util.Query;
import com.yeecloud.meeto.common.util.StringUtils;
import com.yeecloud.meeto.configure.service.ConfigureService;
import com.yeecloud.meeto.ipparser.IPInfo;
import com.yeecloud.meeto.ipparser.service.IPParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Title
 *
 * Date: 2020-09-17 16:12:33
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
@RestController
@RequestMapping("/app/api/v1/c03")
public class ServerController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ServerConvert convert;

    @Autowired
    private AppService appService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private ServerService serverService;
    @Autowired
    private IPParserService ipParserService;
    @Autowired
    private ConfigureService configureService;

    @PostMapping("/c0001")
    public Result getServerList(@RequestHeader(Constants.H_PACKGE_NAME) String pkgName,
                                @RequestHeader(Constants.H_LOCALE) String locale,
                                @RequestHeader(Constants.H_TOKEN) String token) throws ServiceException {
        Integer appId = getAppId(pkgName);
        if (appId == null) {
            return Result.FAILURE(ResultCode.PARAM_ERROR);
        }
        Query query = new Query(Maps.newHashMap());
        query.put("status", Server.State.RUNNING);
        List<Server> list = serverService.query(query);
        List<ServerVO> resultList = transform(list, locale);
        return Result.SUCCESS(resultList);
    }

    @PostMapping("/c0001/{id}")
    public Result<ServerProfileVO> getServerProfile(@RequestHeader(Constants.H_PACKGE_NAME) String pkgName,
                                                    @RequestHeader(Constants.H_LOCALE) String locale,
                                                    @RequestHeader(Constants.H_TOKEN) String token,
                                                    @RequestHeader(Constants.H_UUID) String devId,
                                                    @RequestHeader(Constants.H_IMEI) String imei,
                                                    @PathVariable("id") Integer serverId) throws ServiceException {
        log.info("IpAddr:[{}] PKG:[{}] TOKEN:[{}] UUID:[{}] IMEI:[{}] RequestServer:[{}]", ParamUtils.getIpAddr(request), pkgName, token, devId, imei, serverId);
        Integer appId = getAppId(pkgName);
        if (appId == null) {
            log.info("Cann't found Pkg:[{}]", pkgName);
            return Result.FAILURE(ResultCode.PARAM_ERROR);
        }
        String ipAddress = ParamUtils.getIpAddr(request);
        boolean limit = isNeedRegionLimit(imei, ipAddress);
        if (limit) {
            return Result.FAILURE(getLimitError(isEnglish(locale)));
        }

        Member member = memberService.findMemberByToken(appId, token);
        Device device = deviceService.findDeviceByIMEIOrUUID(imei, devId);
        ServerAccount account = serverService.findServerAccountByServerId(serverId, appId, member != null ? member.getId() : 0, device != null ? device.getId() : 0);
        if (account != null) {
            ServerProfileVO profile = convert.convert(account);
            if (isEnglish(locale)) {
                profile.setName(account.getServer().getNameEn());
            }
            log.info("Account Dispatcher. IPAddr[{}] Server:[{}] Account:[{}] Device:[{}] IMEI:[{}]", ParamUtils.getIpAddr(request), account.getServer().getIpAddr(), account.getUserName(), devId, imei);
            return Result.SUCCESS(profile);
        }
        log.info("No Available Server");
        return Result.FAILURE("No Available Server");
    }

    @RequestMapping("/c0002")
    public Result reportLog(@RequestHeader(Constants.H_PACKGE_NAME) String pkgName,
                            @RequestHeader(Constants.H_LOCALE) String locale,
                            @RequestHeader(Constants.H_TOKEN) String token,
                            @RequestHeader(Constants.H_UUID) String devId,
                            @RequestHeader(Constants.H_IMEI) String imei) {
        String content = ParamUtils.parameterToString(request);
        log.info("IpAddr:[{}] CONNECT_LOG PKG:[{}] TOKEN:[{}] DEVICE:[{}] IMEI:[{}] MSG:[{}]", ParamUtils.getIpAddr(request), pkgName, token, devId, imei, content);
        return Result.SUCCESS();
    }

    private Integer getAppId(String pkgName) throws ServiceException {
        if (StringUtils.isNotBlank(pkgName)) {
            App app = appService.findAppByPkgName(pkgName);
            if (app != null) {
                return app.getId();
            }
        }
        return null;
    }

    private List<ServerVO> transform(List<Server> list, String locale) {
        List<ServerVO> resultList = Lists.newArrayList();
        list.forEach(server -> {
            ServerVO vo = convert.convert(server);
            if (isEnglish(locale)) {
                vo.setName(server.getNameEn());
                vo.setSummary(server.getSummaryEn());
            }
            resultList.add(vo);
        });
        return resultList;
    }

    private boolean isEnglish(String locale) {
        return !locale.toLowerCase().contains("tw");
    }


    private boolean isNeedRegionLimit(String imei, String ipAddress) {
        if (isImeiNoLimit(imei)) {
            return false;
        }
        //获取限制地区
        String value = configureService.getValueByKey("limit.country.deny", "");
        if (StringUtils.isBlank(value)) {
            //未设置限制地区
            return false;
        }
        //解析IP
        IPInfo ipInfo = ipParserService.execute(ipAddress);
        if (StringUtils.contains(value, ipInfo.getCountry())) {
            log.error(" IMEI:[{}] IpAddr:[{}] in Limit Country:[{}]!", imei, ipAddress, ipInfo.getCountry());
            return true;
        }
        return false;
    }

    //IMEI不限
    private boolean isImeiNoLimit(String imei) {
        if (StringUtils.length(imei) != 15) {
            return false;
        }
        String value = configureService.getValueByKey("limit.imei.allow", "");
        if (StringUtils.contains(value, imei)) {
            return true;
        }
        return false;
    }

    private String getLimitError(boolean english) {
        if (english) {
            return configureService.getValueByKey("limit.hint.en", "Sorry! because the policy, we cannot provide servces for your region!");
        }
        return configureService.getValueByKey("limit.hint.cn", "非常抱嵌,因為政策原因,你所在的地區無法提供服務!");
    }
}