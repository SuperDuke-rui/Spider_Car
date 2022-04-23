package com.spider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spider.mapper.PageUrlsMapper;
import com.spider.pojo.PageUrls;
import com.spider.service.IPageUrlsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author wangrui
 * @Description “二手车之家”分页页面Service
 * @date 2022/4/9 22:24
 */
@Service
public class PageUrlsService implements IPageUrlsService {

    @Resource
    private PageUrlsMapper pageUrlsMapper;

    /**
     * 获取所有urls
     * @return
     */
    @Override
    public List<PageUrls> getUrls() {
        QueryWrapper<PageUrls> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*");
        return pageUrlsMapper.selectList(queryWrapper);
    }

    @Override
    public int insert(PageUrls pageUrls) {
        return pageUrlsMapper.insert(pageUrls);
    }

    /**
     * 从page_urls表中获取品牌名
     * @return
     */
    @Override
    public List<Object> brandsFromPageUrls() {
        QueryWrapper<PageUrls> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("brand")
                .groupBy("brand")
                .orderByDesc("COUNT(brand)");
        return pageUrlsMapper.selectObjs(queryWrapper);
    }

    /**
     * 通过品牌获取到所有品牌的url
     * @param brand 需要查询的品牌
     * @return
     */
    @Override
    public List<PageUrls> getUrlsOfBrand(String brand) {
        return pageUrlsMapper.getUrlsOfBrand(brand);
    }
}
