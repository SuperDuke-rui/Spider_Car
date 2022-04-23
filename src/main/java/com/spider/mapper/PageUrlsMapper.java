package com.spider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spider.pojo.PageUrls;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/12 15:24
 */
public interface PageUrlsMapper extends BaseMapper<PageUrls> {

    /*获取指定品牌的url*/
    @Select("SELECT * FROM page_urls WHERE brand = #{brand}")
    List<PageUrls> getUrlsOfBrand(String brand);
}
