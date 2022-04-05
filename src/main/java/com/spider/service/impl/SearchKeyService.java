package com.spider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spider.mapper.SearchKeyMapper;
import com.spider.pojo.SearchKey;
import com.spider.service.ISearchKeyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    /**
     * 获取搜索量前十的热搜关键词
     * @return
     */
    @Override
    public List<Map<String, Object>> queryTopTen() {
        QueryWrapper<SearchKey> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("words","COUNT(words) num")
                .groupBy("words")
                .orderByDesc("COUNT(words)")
                .last("limit 10");
        return searchKeyMapper.selectMaps(queryWrapper);
    }
}
