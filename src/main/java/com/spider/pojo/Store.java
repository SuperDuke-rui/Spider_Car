package com.spider.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author wangrui
 * @Description 用于保存用户收藏的表的实体类，
 * 其中的字段为：Car实体类的全部字段 + 用户uid +
 * 收藏时间save_time + 逻辑删除用到的state（1：正常；0：逻辑删除）
 * @date 2022/3/27 22:18
 */
@TableName("user_store")
@Data
public class Store {
    /*用户uid*/
    private int uid;

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

    /*收藏时间*/
    private String saveTime;

    /*状态 1：正常；0：逻辑删除*/
    private int state;
}
