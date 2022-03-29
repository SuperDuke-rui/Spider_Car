package com.spider.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author wangrui
 * @Description 保存搜索的关键词
 * @date 2022/3/29 9:55
 */
@TableName("search_keys")
@Data
public class SearchKey {
    /*关键词id*/
    private int id;

    /*用户搜索的内容字段*/
    private String words;

    /*用户搜索的时间*/
    private String searchTime;
}
