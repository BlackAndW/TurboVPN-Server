package com.mobplus.greenspeed.module.gateway.controller;

import com.mobplus.greenspeed.entity.Ip2location;
import com.mobplus.greenspeed.repository.Ip2locationRepository;
import com.mobplus.greenspeed.util.IpUtils;
import com.mobplus.greenspeed.util.ProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

/**
 * @author: Leonard
 * @create: 2021/6/9
 */
@Slf4j
@RestController
@RequestMapping("/app/api/v1")
public class LinkAccessInfoController {

    @Autowired
    Ip2locationRepository ip2locationRepository;

    @RequestMapping("makeLogFile")
    public void makeLogFile() {
        ProcessUtils.execute("/root/shell/make_link_log.sh");
    }

    @RequestMapping("linkInfo")
    public void modifyLogFile() throws IOException {
        //读取文件(字符流)
//        BufferedReader in = new BufferedReader(new FileReader("d:\\1-project\\link-info.log"));
        BufferedReader in = new BufferedReader(new FileReader("/www/wwwroot/apk.siteps.cn/link-info.log"));
        //写入相应的文件
//        BufferedWriter out = new BufferedWriter(new FileWriter("d:\\1-project\\link-info-detail.log"));
        BufferedWriter out = new BufferedWriter(new FileWriter("/www/wwwroot/apk.siteps.cn/link-info-detail.log"));
        makeLinkInfo(in, out, " --- ", 0);

        //生成ip统计
//        BufferedReader in = new BufferedReader(new FileReader("d:\\1-project\\link-info-c.log"));
        BufferedReader in2 = new BufferedReader(new FileReader("/www/wwwroot/apk.siteps.cn/link-info-c.log"));
        //写入相应的文件
//        BufferedWriter out = new BufferedWriter(new FileWriter("d:\\1-project\\link-info-count.log"));
        BufferedWriter out2 = new BufferedWriter(new FileWriter("/www/wwwroot/apk.siteps.cn/link-info-count.log"));
        makeLinkInfo(in2, out2, " ", 1);
    }

    private void makeLinkInfo(BufferedReader in, BufferedWriter out, String regex, int ipIndex) throws IOException {

        String str = null;
        //读取数据
        //循环取出数据
        while ((str = in.readLine()) != null) {
            str = str.trim();
            String ipAddress = str.split(regex)[ipIndex];
            if (ipAddress != null && ipAddress.length() > 0) {
                long ipLong = IpUtils.ipStr2long(ipAddress);
                Ip2location ipInfo = ip2locationRepository.findIpInfo(ipLong);
                if (ipIndex != 1) {
                    str = ipInfo.getCountry() + " --> " + ipInfo.getRegion() + " --> " + ipInfo.getCity() + " --- " + str;
                } else {
                    str = ipInfo.getCountry() + " --> " + ipInfo.getRegion() + " --> " + ipInfo.getCity() + " --- " + ipAddress + " --- " + str.split(regex)[0];
                }
            }
            //写入相关文件
            out.write(str);
            out.newLine();
        }

        //清理缓存
        out.flush();
        //关闭流
        in.close();
        out.close();
    }
}
