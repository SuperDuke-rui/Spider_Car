package com.spider.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author wangrui
 * @Description Jsoup爬取数量很大，使用动态ip代理工具类
 * @date 2022/3/10 15:37
 */
public class HttpUtils {

    /**
     * 设置代理ip
     * @throws IOException
     */
    public static void setProxyIp() throws IOException {
        try {
            List<String> ipList = new ArrayList<>();
            ClassPathResource cpr = new ClassPathResource("ips.txt");
            InputStream ips = cpr.getInputStream();
            BufferedReader proxyIpReader = new BufferedReader(new InputStreamReader(ips));
            String https = "";
            while ((https = proxyIpReader.readLine()) != null) {
                ipList.add(https);
            }

            Random random = new Random();
            int randomInt = random.nextInt(ipList.size());
            String ipPort = ipList.get(randomInt);
            String proxyIp = ipPort.substring(0, ipPort.lastIndexOf(":"));
            String proxyPort = ipPort.substring(ipPort.lastIndexOf(":") + 1, ipPort.length());

            System.setProperty("http.maxRedirects","50");
            System.getProperties().setProperty("proxySet", "true");
            System.getProperties().setProperty("http.proxyHost", proxyIp);
            System.getProperties().setProperty("http.proxyPort", proxyPort);

            System.out.println("设置代理ip为：" + proxyIp + "，端口号为：" + proxyPort);

        } catch (Exception e) {
            System.out.println("重新设置代理ip");
            setProxyIp();
        }
    }
}
