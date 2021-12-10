package com.mobplus.greenspeed.module.gateway.controller;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mobplus.greenspeed.Constants;
import com.mobplus.greenspeed.entity.*;
import com.mobplus.greenspeed.module.gateway.convert.ServerConvert;
import com.mobplus.greenspeed.module.gateway.vo.ServerProfileVO;
import com.mobplus.greenspeed.module.gateway.vo.ServerVO;
import com.mobplus.greenspeed.repository.Ip2locationRepository;
import com.mobplus.greenspeed.service.*;
import com.mobplus.greenspeed.util.IpUtils;
import com.mobplus.greenspeed.util.Result;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.ResultCode;
import com.yeecloud.meeto.common.util.ParamUtils;
import com.yeecloud.meeto.common.util.Query;
import com.yeecloud.meeto.common.util.StringUtils;
import com.yeecloud.meeto.configure.service.ConfigureService;
import com.yeecloud.meeto.ipparser.IPInfo;
import com.yeecloud.meeto.ipparser.service.IPParserService;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 *  VPN节点管理
 * @author Leonard
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
    private ServerRESTService serverRESTService;
    @Autowired
    private Ip2locationRepository ip2locationRepository;
    @Autowired
    private ConfigureService configureService;

    /**
     * 获取普通节点列表
     * @param mobileOS  【header参数-mobileOS】手机系统 传android(默认)或ios
     * @param pkgNameReal   【header参数-H006】 真实包名
     * @return
     * @throws ServiceException
     */
    @PostMapping("/c0001")
    public Result getServerList(@RequestHeader(value = Constants.H_PACKGE_NAME, defaultValue = "com.freetech.turbovpn") String pkgName,
                                @RequestHeader(value = Constants.H_LOCALE, defaultValue = "") String locale,
                                @RequestHeader(value = Constants.H_MOIBILE_OS, defaultValue = "android") String mobileOS,
                                @RequestHeader(value = Constants.H_PACKGE_NAME_REAL, defaultValue = "com.freetech.turbovpn") String pkgNameReal,
                                @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion)  throws ServiceException {

        return getResultList(pkgName, locale, mobileOS, Server.Type.NORMAL, pkgNameReal, apiVersion);
    }

    /**
     *
     * 获取VIP节点列表
     * @param pkgNameReal   【header参数-H006】 真实包名
     * @param mobileOS  【header参数-mobileOS】手机系统 传android(默认)或ios
     * @return
     * @throws ServiceException
     */
    @PostMapping("/vip")
    public Result getVipServerList(@RequestHeader(value = Constants.H_PACKGE_NAME, defaultValue = "com.freetech.turbovpn") String pkgName,
                                   @RequestHeader(value = Constants.H_LOCALE, defaultValue = "") String locale,
                                   @RequestHeader(value = Constants.H_MOIBILE_OS, defaultValue = "android") String mobileOS,
                                   @RequestHeader(value = Constants.H_PACKGE_NAME_REAL, defaultValue = "com.freetech.turbovpn") String pkgNameReal,
                                   @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) throws ServiceException {
        return getResultList(pkgName, locale, mobileOS, Server.Type.VIP, pkgNameReal, apiVersion);
    }

    /**
     *
     * @param type      节点类型
     * @return
     * @throws ServiceException
     */
    private Result getResultList(String pkgName, String locale, String mobileOS, Integer type, String pkgNameReal, String apiVersion) throws ServiceException{
        String ipAddress = request != null && !locale.equals("local")? ParamUtils.getIpAddr(request) : "";
        boolean limit = isNeedRegionLimit(ipAddress);
        if (limit) {
            return Result.FAILURE(getLimitError(isEnglish(locale)));
        }
        Integer appId = getAppId(pkgName);
        if (appId == null) {
            return Result.FAILURE(ResultCode.PARAM_ERROR);
        }
        Query query = new Query(Maps.newHashMap());
        query.put("status", Server.State.RUNNING);
        query.put("type", type);
        query.put("mobileOS", mobileOS);
        List<Server> list = serverService.query(query);
        if (pkgNameReal != null && pkgNameReal.length() > 0) {
            // 根据app配置筛除节点
            List<Server> serverFilterBySetting = serverRESTService.filterBySetting(pkgNameReal, list);
            list = serverRESTService.sortByOrder(pkgNameReal, serverFilterBySetting);
        }
        List<ServerVO> resultList = transform(list, locale);
        return Result.isEncode(apiVersion, resultList);
    }

    /**
     * 获取自动/手动连接节点信息
     * @param pkgNameReal   【header参数-H006】 真实包名，默认值为原始包名
     * @param serverId 【url参数】节点id,0代表自动连接
     * @return
     * @throws ServiceException
     * @throws IOException
     */
    @PostMapping("/c0001/{id}")
    public Result getServerProfile(@RequestHeader(value = Constants.H_PACKGE_NAME, defaultValue="com.freetech.turbovpn") String pkgName,
                                                    @RequestHeader(value = Constants.H_LOCALE, defaultValue="") String locale,
                                                    @RequestHeader(value = Constants.H_TOKEN, defaultValue="") String token,
                                                    @RequestHeader(value = Constants.H_UUID, defaultValue="") String devId,
                                                    @RequestHeader(value = Constants.H_IMEI, defaultValue="") String imei,
                                                    @RequestHeader(value = Constants.H_PACKGE_NAME_REAL, defaultValue = "com.freetech.turbovpn") String pkgNameReal,
                                                    @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion,
                                                    @PathVariable(value = "id", required = true) Integer serverId) throws ServiceException, IOException {
        String ipAddress = request != null && !locale.equals("local")? ParamUtils.getIpAddr(request) : "";
        boolean limit = isNeedRegionLimit(ipAddress);
        if (limit) {
            return Result.FAILURE(getLimitError(isEnglish(locale)));
        }
        log.info("IpAddr:[{}] PKG:[{}] TOKEN:[{}] UUID:[{}] IMEI:[{}] RequestServer:[{}]", ipAddress, pkgName, token, devId, imei, serverId);
        Integer appId = getAppId(pkgName);
        if (appId == null) {
            log.info("Cann't found Pkg:[{}]", pkgName);
            return Result.FAILURE(ResultCode.PARAM_ERROR);
        }

        Member member = memberService.findMemberByToken(appId, token);
        Device device = deviceService.findDeviceByIMEIOrUUID(imei, devId);
        ServerAccount account = serverService.findServerAccountByServerId(
                serverId, appId,
                member != null ? member.getId() : 0,
                device != null ? device.getId() : 0,
                ipAddress,
                pkgNameReal
        );
        if (account != null) {
            ServerProfileVO profile = convert.convert(account);
            if (isEnglish(locale)) {
                profile.setName(account.getServer().getNameEn());
            }
            log.info("Account Dispatcher. IPAddr[{}] Server:[{}] Account:[{}] Device:[{}] IMEI:[{}]", ipAddress, account.getServer().getIpAddr(), account.getUserName(), devId, imei);
            return Result.isEncode(apiVersion, profile);
        }
        log.info("No Available Server");
        return Result.FAILURE("No Available Server");
    }

    /**
     * 废弃接口
     * @param pkgName 【header参数-H001】假包名
     * @param token     【header参数-H002】token
     * @param devId     【header参数-H003】设备id
     * @param imei      【header参数-H004】imei
     * @return
     */
    @RequestMapping("/c0002")
    public Result reportLog(@RequestHeader(Constants.H_PACKGE_NAME) String pkgName,
                            @RequestHeader(Constants.H_LOCALE) String locale,
                            @RequestHeader(Constants.H_TOKEN) String token,
                            @RequestHeader(Constants.H_UUID) String devId,
                            @RequestHeader(Constants.H_IMEI) String imei,
                            @RequestHeader(value = "Api-Version", defaultValue = "1.0") String apiVersion) {
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
            // log.info(vo.toString());
            vo.setServerName(server.getName());
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


    private boolean isNeedRegionLimit(String ipAddress) {
        //获取限制地区
        String value = configureService.getValueByKey("limit.country.deny", "");
        if (StringUtils.isBlank(value)) {
            //未设置限制地区
            return false;
        }
        //解析IP,测试用ip，生产环境需要注释掉
        ipAddress = "103.137.150.238";
        Ip2location ip2location = ip2locationRepository.findIpInfo(IpUtils.ipStr2long(ipAddress));
        if (StringUtils.contains(value, ip2location.getCountryCode())) {
            log.error(" IMEI:[{}] IpAddr:[{}] in Limit Country:[{}]!", "no imei", ipAddress, ip2location.getCountryCode());
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
            return configureService.getValueByKey("limit.hint.en", "Sorry! because the policy, we cannot provide services for your region!");
        }
        return configureService.getValueByKey("limit.hint.cn", "非常抱歉,因為政策原因,你所在的地區無法提供服務!");
    }
}
