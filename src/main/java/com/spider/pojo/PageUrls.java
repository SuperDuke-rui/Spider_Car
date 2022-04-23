package com.spider.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/12 15:25
 */
@TableName("page_urls")
@Data
public class PageUrls {
    //id
    @TableId(type = IdType.AUTO)
    private int id;

    //pageUrl
    private String pagesUrl;

    //brand
    private String brand;
}
