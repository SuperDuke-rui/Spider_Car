package com.spider.service;

import com.spider.pojo.SearchKey;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/29 10:01
 */
public interface ISearchKeyService {
    /**
     * 向数据库中插入数据
     * @param searchKey 要插入的searchKey类
     * @return
     */
    int insertKey(SearchKey searchKey);

    /**
     * 获取搜索量前十的热搜关键词
     * @return
     */
    List<Map<String, Object>> queryTopTen();
}
