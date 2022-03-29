package com.spider.service.impl;

import com.spider.mapper.SearchKeyMapper;
import com.spider.pojo.SearchKey;
import com.spider.service.ISearchKeyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/29 10:01
 */
@Service
public class SearchKeyService implements ISearchKeyService {

    @Resource
    private SearchKeyMapper searchKeyMapper;

    /**
     * 向数据库中插入数据
     * @param searchKey 要插入的searchKey类
     * @return
     */
    @Override
    public int insertKey(SearchKey searchKey) {
        return searchKeyMapper.insert(searchKey);
    }
}
