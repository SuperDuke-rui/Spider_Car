package com.spider.dao;

import com.spider.utils.C3P0Util;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @Author wangrui
 * @Description 处理Car的CRUD的dao
 * @date 2022/3/8 15:18
 */
public class CarDao {

    //初始化JdbcTemplate模板
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(C3P0Util.getDataSource());



}
