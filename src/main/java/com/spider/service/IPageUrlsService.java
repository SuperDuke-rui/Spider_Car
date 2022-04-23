package com.spider.service;

import com.spider.pojo.PageUrls;

import java.util.List;

/**
 * @Author wangrui
 * @Description “二手车之家”分页页面Service
 * @date 2022/4/9 22:23
 */
public interface IPageUrlsService {

    /**
     * 获取所有urls
     * @return
     */
    List<PageUrls> getUrls();

    /**
     * 插入url
     * @param pageUrls
     * @return
     */
    int insert(PageUrls pageUrls);

    /**
     * 从page_urls表中获取品牌名
     * @return
     */
    List<Object> brandsFromPageUrls();

    /**
     * 通过品牌获取到所有品牌的url
     * @param brand 需要查询的品牌
     * @return
     */
    List<PageUrls> getUrlsOfBrand(String brand);

}
