package com.mobplus.greenspeed.module.gateway.controller;

import com.apache.commons.beanutils.NewBeanUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mobplus.greenspeed.entity.AppSetting;
import com.mobplus.greenspeed.entity.Server;
import com.mobplus.greenspeed.module.gateway.convert.ServerConvert;
import com.mobplus.greenspeed.module.gateway.form.ServerForm;
import com.mobplus.greenspeed.module.gateway.vo.ServerVO;
import com.mobplus.greenspeed.module.gateway.vo.SettingVO;
import com.mobplus.greenspeed.service.AppService;
import com.mobplus.greenspeed.service.ServerRESTService;
import com.mobplus.greenspeed.service.ServerService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author: Leonard
 * @create: 2021/8/23
 */

@Slf4j
@RestController
@RequestMapping("/app/api/v1/c04")
public class ServerRESTController {

    @Autowired
    private ServerConvert convert;

    @Autowired
    private ServerService serverService;

    @Autowired
    private ServerRESTService serverRESTService;

    @GetMapping("/list")
    public Result getServerList(@RequestParam String pkgName, @RequestParam Integer type, boolean filterFlag) throws ServiceException {
        Query query = new Query(Maps.newHashMap());
        query.put("type", type);
        query.put("clearCache", true);
        List<Server> list = serverService.query(query);
        // 配置接口，不过滤节点，返回所有
        list = !filterFlag ? serverRESTService.filterBySetting(pkgName, list) : list;
        list = serverRESTService.sortByOrder(pkgName, list);
        List<ServerVO> resultList = convert.convert(list);
        return Result.SUCCESS(resultList);
    }

    /***
     * 配置接口，返回所有
     * @param pkgName
     * @return
     * @throws ServiceException
     */
    @GetMapping("/s/list")
    public Result getAllServer(@RequestParam String pkgName) throws ServiceException {
        return getServerList(pkgName, Server.Type.ALL, true);
    }

    @GetMapping("/setting")
    public SettingVO getServerAppSetting(@RequestParam String pkgName) throws ServiceException {
        return serverRESTService.getSettingByApp(pkgName);
    }
    @PostMapping("/setting/update")
    public Result updateServerAppSetting(@RequestParam String pkgName, @RequestBody Map<String, Object> params) throws ServiceException {
        Query query = new Query(params);
        serverRESTService.updateSettingByApp(pkgName, query);
        return Result.SUCCESS();
    }


    @PostMapping("/create")
    public Result create(@RequestBody @Valid ServerForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        serverRESTService.create(form);
        return Result.SUCCESS();
    }

    @PostMapping("/update/{id}")
    public Result update(@PathVariable Integer id, @RequestBody @Valid ServerForm form, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            String message = String.format("操作失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        serverRESTService.update(id, form);
        return Result.SUCCESS();
    }

    @PostMapping("/update/online")
    public void updateOnlineConn(@RequestParam(value = "ip_addr") String ipAddr, @RequestParam(value = "online_conn") Integer onlineConn) throws ServiceException {
        serverRESTService.updateOnlineConn(ipAddr, onlineConn);
    }

}
