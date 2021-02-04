package com.mobplus.greenspeed.common.convert;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Title
 *
 * Date: 2019-11-18 02:32:26
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
public class GeneralConvert {
    public JSONObject str2Json(String content) {
        try {
            if (content != null && StringUtils.isNotBlank(content)) {
                return JSONObject.parseObject(content);
            }
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public String json2Str(JSONObject content) {
        try {
            if (content != null) {
                content.toJSONString();
            }
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public List<String> json2List(String content) {
        try {
            if (content != null && StringUtils.isNotBlank(content)) {
                return JSON.parseArray(content, String.class);
            }
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public String list2Json(List<String> content) {
        try {
            if (content != null && !content.isEmpty()) {
                return JSON.toJSONString(content);
            }
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

//    public Resource.Ads pathToUrl(String path) {
//        return new Resource.Ads(path);
//    }
//
//    public List<Resource.Goods> json2ResList(String content) {
//        try {
//            List<String> list = json2List(content);
//            if (list != null) {
//                return list.stream().map(ele -> {
//                    return new Resource.Goods(ele);
//                }).collect(Collectors.toList());
//            }
//        } catch (JSONException e) {
//            log.error(e.getMessage(), e);
//        }
//        return null;
//    }
}
