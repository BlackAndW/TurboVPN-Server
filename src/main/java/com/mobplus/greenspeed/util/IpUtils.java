package com.mobplus.greenspeed.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;

/**
 * @author: Leonard
 * @create: 2021/4/27
 */
@Slf4j
public class IpUtils {

    public static Long ipStr2long (String ipStr) {
        long ipLong = 0;

        if (ipStr != null && ipStr.length() > 0) {
            //将ip地址按.分割
            String[] ipSplit = ipStr.split("\\.");
            try {
                if (ipSplit.length != 4) {
                    throw new Exception("IP Format Error");
                }
                // 将数组里的字符串强转为long类型后执行：x^24+y^16+z^8+o, <<：位运算符（左移）
                ipLong = (Long.parseLong(ipSplit[0]) << 24)
                        + (Long.parseLong(ipSplit[1]) << 16)
                        + (Long.parseLong(ipSplit[2]) << 8)
                        + (Long.parseLong(ipSplit[3]));
            } catch (Exception e) {
                log.error("ip address to long type Error :{}", e);
            }
        } else {
            log.info("ip address is null or is ''");
        }
        return ipLong;
    }

    public static String ipLong2Str(long ipLong) {
        StringBuffer ipStr = new StringBuffer();
        try {
            if (ipLong < 0) {
                throw new Exception("Can not to IP...");
            }
            //ip第一位：整数直接右移24位。
            //ip第二位：整数先高8位置0.再右移16位。
            //ip第三位：整数先高16位置0.再右移8位。
            //ip第四位：整数高24位置0.
            ipStr.append((ipLong>>24)).append(".").
                    append(((ipLong&0x00ffffff)>>16)).append(".").
                    append(((ipLong&0x0000ffff)>>8)).append(".").
                    append((ipLong&0x000000ff));
        } catch (Exception e) {
            log.error("Long type ip to point ten type error :{}", e);
        }
        return ipStr.toString();
    }

    // 淘宝ip接口调用，有QPS限制，弃用
//    private JSONObject getIpInfo(String ip) throws IOException {
//        OkHttpClient client = new OkHttpClient();
//        FormBody.Builder formBuilder = new FormBody.Builder();
//        formBuilder.add("ip", ip);
//        formBuilder.add("accessKey", "alibaba-inc");
//        Request request = new Request.Builder()
//                .url("https://ip.taobao.com/outGetIpInfo")
//                .post(formBuilder.build())
//                .build();
//        String resultJson = client.newCall(request).execute().body().string();
//        return JSONObject.parseObject(resultJson).getJSONObject("data");
//    }
}
