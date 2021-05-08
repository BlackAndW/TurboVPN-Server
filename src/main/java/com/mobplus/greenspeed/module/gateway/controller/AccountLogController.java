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
import com.mobplus.greenspeed.util.ProcessUtils;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.DateUtils;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: Leonard
 * @create: 2021/4/28
 */

@Slf4j
@RestController
@RequestMapping("/app/api/v1/vpn")
public class AccountLogController {

    @Value("${spring.datasource.password}")
    private String password;

    @Autowired
    private ServerService serverService;

    @GetMapping("/list")
    public JSONObject getServerList(@RequestParam Map<String, Object> params) throws ServiceException, ParseException {
        Query query = new Query(Maps.newHashMap(params));
        Page<AccountLog> list = serverService.queryLog(query);
        PageInfo<AccountLogVO> resultList = convert(list);
        // 下划线转驼峰，（默认下划线的原因未知）
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
        String response = JSON.toJSONString(resultList, config);
        return JSON.parseObject(response);
    }

    @GetMapping("data2excel")
    public JSONObject getAccountExcel(@RequestParam Map<String, Object> params) throws ParseException {
        StringBuilder sb = new StringBuilder();
        Query query = new Query(Maps.newHashMap(params));
        sb.append("mysql -uroot -p").append(password).append(" -e \"select ").
                append("n_user_ip as 用户ip（未转化）, ").
                append("n_country as 所属国家, ").
                append("n_region as 地区, ").
                append("n_city as 城市, ").
                append("n_server_name as 节点名称, ").
                append("from_unixtime(n_created_at/1000, '%Y-%m-%d %H:%i:%s') as 日期 ").
                append("from t_server_account_log ").
                append("where 1=1 ");
        if (query.containsKey("userIp")) {
            sb.append(" and n_user_ip=").append(IpUtils.ipStr2long(query.get("userIp", String.class)));
        } else if (query.containsKey("country")) {
            sb.append(" and n_country='").append(query.get("country")).append("'");
        } else if (query.containsKey("region")) {
            sb.append(" and n_region='").append(query.get("region")).append("'");
        } else  if (query.containsKey("city")) {
            sb.append(" and n_city='").append(query.get("city")).append("'");
        } else if (query.containsKey("serverName")) {
            sb.append(" and n_server_name='").append(query.get("serverName")).append("'");
        } else if (query.containsKey("startTimeStr") && query.containsKey("endTimeStr")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long startTime = simpleDateFormat.parse(query.get("startTimeStr", String.class)).getTime();
            long endTime = simpleDateFormat.parse(query.get("endTimeStr", String.class)).getTime();
            sb.append(" and n_created_at between ").append(startTime).append(" and ").append(endTime);
        } else if (query.containsKey("pkgName")) {
            sb.append(" and n_pkg_name_real='").append(query.get("pkgName")).append("'");
        }
        final String rootPath = "/www/wwwroot/res.turbovpns.com/";
        final String filePath = "download/accountLogFiles/";
        final String fileName = DateUtils.dateToString(new Date(), "yyyyMMddHHmmss") + ".xls";
        sb.append(" \" green_speed > ").append(rootPath).append(filePath).append(fileName);
        ProcessUtils.execute(sb.toString());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("file", filePath+fileName);
        return jsonObject;
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
