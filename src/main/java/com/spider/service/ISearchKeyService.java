package com.spider.service;

import com.spider.pojo.SearchKey;

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
}
