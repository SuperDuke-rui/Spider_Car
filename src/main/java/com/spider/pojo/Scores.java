package com.spider.pojo;

import lombok.Data;

/**
 * @Author wangrui
 * @Description 对比表评分包装类
 * @date 2022/4/19 8:21
 */
@Data
public class Scores {
    /*价格分数*/
    double priceScore;

    /*表显里程分数*/
    double mileScore;

    /*排量*/
    double emissionScore;

    /*过户次数分数*/
    double transScore;

    /*发动机功率分数*/
    double powerScore;

    /*发动机缸数分数*/
    double numScore;

    /*总分*/
    double totalScore;
}
