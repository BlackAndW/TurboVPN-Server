package com.mobplus.greenspeed.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author: Leonard
 * @create: 2021/5/7
 */

@Slf4j
public class ProcessUtils {

    public static void execute(String cmd) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(cmd);
        //将标准输入流和错误输入流合并，通过标准输入流读取信息
        builder.redirectErrorStream(true);
        try {
            //启动进程
            Process start = builder.start();
            //获取输入流
            InputStream inputStream = start.getInputStream();
            //转成字符输入流
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "gbk");
            int len = -1;
            char[] c = new char[1024];
            StringBuffer outputString = new StringBuffer();
            //读取进程输入流中的内容
            while ((len = inputStreamReader.read(c)) != -1) {
                String s = new String(c, 0, len);
                outputString.append(s);
            }
            log.info("outputString: " + outputString.toString());
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
