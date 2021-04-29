package com.mobplus.greenspeed.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: Leonard
 * @create: 2021/4/27
 */
@Slf4j
public class IpUtils {

    public static Long ipStr2long (String ipStr) {
        long ipLong = 0;

        if (ipStr != null && ipStr.length() > 0 && !ipStr.equals("0:0:0:0:0:0:0:1")) {
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

    private static String ipLong2String(long ipLong) {
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
}
