package com.mobplus.greenspeed.module.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.apache.commons.beanutils.NewBeanUtils;
import com.google.common.collect.Maps;
import com.mobplus.greenspeed.entity.AccountLog;
import com.mobplus.greenspeed.entity.Server;
import com.mobplus.greenspeed.module.gateway.vo.AccountLogVO;
import com.mobplus.greenspeed.module.gateway.vo.ServerVO;
import com.mobplus.greenspeed.service.ServerService;
import com.mobplus.greenspeed.util.IpUtils;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Leonard
 * @create: 2021/4/28
 */

@Slf4j
@RestController
@RequestMapping("/app/api/v1/vpn")
public class AccountLogController {

    @Autowired
    private ServerService serverService;

    @GetMapping("/list")
    public JSONObject getServerList(@RequestParam Map<String, Object> params) throws ServiceException, ParseException {
        Query query = new Query(Maps.newHashMap(params));
        System.out.println(query);
        Page<AccountLog> list = serverService.queryLog(query);
        PageInfo<AccountLogVO> resultList = convert(list);
        // 下划线转驼峰，（默认下划线的原因未知）
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
        String response = JSON.toJSONString(resultList, config);
        return JSON.parseObject(response);
    }

    private PageInfo<AccountLogVO> convert(Page<AccountLog> result) {
        List<AccountLogVO> resultList = new ArrayList<>();
        result.getContent().forEach( item -> {
            AccountLogVO accountLogVO = new AccountLogVO();
            NewBeanUtils.copyProperties(accountLogVO, item);
            accountLogVO.setIp(IpUtils.ipLong2Str(item.getUserIp()));
            resultList.add(accountLogVO);
        });
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }
}
