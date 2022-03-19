package com.spider.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/8 11:46
 */
@TableName("car_detail")
@Data
public class Car {
    /*二手车id,用于唯一识别二手车信息*/
    private int cid;

    /*二手车标题描述*/
    private String title;

    /*二手车图片*/
    private String carPhoto;

    /*车辆价格 单位：万元*/
    private double carPrice;

    /*车辆品牌 如：奔驰*/
    private String carBrand;

    /*车辆类型 如：奔驰GLC*/
    private String carType;

    /*车辆表显里程 单位：万公里*/
    private String displayedMileage;

    /*车辆上牌时间*/
    private String licensingTime;

    /*车辆变速箱类型*/
    private String transmission;

    /*车辆排放量 单位：L(升)*/
    private double emissions;

    /*车辆排放标准 如：欧V*/
    private String emissionStandard;

    /*车辆年检到期时间，作为字符串获取*/
    private String annualTimeout;

    /*车辆保险到期时间，作为字符串获取*/
    private String insuranceTimeout;

    /*车辆质检到期时间，作为字符串获取*/
    private String qualityTimeout;

    /*车辆过户次数，作为字符串获取*/
    private String transfersTimes;

    /*车辆所在地*/
    private String carLoc;

    /*车辆级别 如：中型SUV*/
    private String carGrade;

    /*车辆发动机 如：2.0T 211马力 L4*/
    private String carEngine;

    /*车辆颜色 如：白色*/
    private String carColor;

    /*燃油标号 如：95号*/
    private String fuelType;

    /*驱动方式 如：前置四驱*/
    private String powerType;

    /*二手车网站*/
    private String carWebsite;

    /*二手车信息发布时间*/
    private String releaseTime;

}
