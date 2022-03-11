package com.spider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.spider.mapper")
@SpringBootApplication
public class SpiderCarApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpiderCarApplication.class, args);
    }

}
