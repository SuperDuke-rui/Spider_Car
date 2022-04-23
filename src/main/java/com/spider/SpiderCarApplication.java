package com.spider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.spider.mapper")
@EnableScheduling
//MapperScan的作用是扫描mapper包下的接口交给spring管理
@SpringBootApplication
public class SpiderCarApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpiderCarApplication.class, args);
    }

}
