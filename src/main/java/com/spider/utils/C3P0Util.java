package com.spider.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/8 15:15
 */
public class C3P0Util {
    private static DataSource dataSource = new ComboPooledDataSource();

    public static DataSource getDataSource(){
        return dataSource;
    }
}
