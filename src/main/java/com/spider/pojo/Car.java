package com.spider.pojo;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/8 11:46
 */
public class Car {
    /*二手车id,用于唯一识别二手车信息*/
    private String cid;

    /*二手车标题描述*/
    private String title;

    /*二手车图片*/
    private String car_photo;

    /*车辆价格 单位：万元*/
    private double car_price;

    /*车辆品牌 如：奔驰*/
    private String car_brand;

    /*车辆类型 如：奔驰GLC*/
    private String car_type;

    /*车辆表显里程 单位：万公里*/
    private String displayed_mileage;

    /*车辆上牌时间*/
    private String licensing_time;

    /*车辆变速箱类型*/
    private String transmission;

    /*车辆排放量 单位：L(升)*/
    private double emissions;

    /*车辆排放标准 如：欧V*/
    private String emission_standard;

    /*车辆年检到期时间，作为字符串获取*/
    private String annual_timeout;

    /*车辆保险到期时间，作为字符串获取*/
    private String insurance_timeout;

    /*车辆质检到期时间，作为字符串获取*/
    private String quality_timeout;

    /*车辆过户次数，作为字符串获取*/
    private String transfers_times;

    /*车辆所在地*/
    private String car_loc;

    /*车辆级别 如：中型SUV*/
    private String car_grade;

    /*车辆发动机 如：2.0T 211马力 L4*/
    private String car_engine;

    /*车辆颜色 如：白色*/
    private String car_color;

    /*燃油标号 如：95号*/
    private String fuel_type;

    /*驱动方式 如：前置四驱*/
    private String power_type;

    /*二手车网站*/
    private String car_website;

    /*二手车信息发布时间*/
    private String release_time;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCar_photo() {
        return car_photo;
    }

    public void setCar_photo(String car_photo) {
        this.car_photo = car_photo;
    }

    public double getCar_price() {
        return car_price;
    }

    public void setCar_price(double car_price) {
        this.car_price = car_price;
    }

    public String getCar_brand() {
        return car_brand;
    }

    public void setCar_brand(String car_brand) {
        this.car_brand = car_brand;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getDisplayed_mileage() {
        return displayed_mileage;
    }

    public void setDisplayed_mileage(String displayed_mileage) {
        this.displayed_mileage = displayed_mileage;
    }

    public String getLicensing_time() {
        return licensing_time;
    }

    public void setLicensing_time(String licensing_time) {
        this.licensing_time = licensing_time;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public double getEmissions() {
        return emissions;
    }

    public void setEmissions(double emissions) {
        this.emissions = emissions;
    }

    public String getEmission_standard() {
        return emission_standard;
    }

    public void setEmission_standard(String emission_standard) {
        this.emission_standard = emission_standard;
    }

    public String getAnnual_timeout() {
        return annual_timeout;
    }

    public void setAnnual_timeout(String annual_timeout) {
        this.annual_timeout = annual_timeout;
    }

    public String getInsurance_timeout() {
        return insurance_timeout;
    }

    public void setInsurance_timeout(String insurance_timeout) {
        this.insurance_timeout = insurance_timeout;
    }

    public String getQuality_timeout() {
        return quality_timeout;
    }

    public void setQuality_timeout(String quality_timeout) {
        this.quality_timeout = quality_timeout;
    }

    public String getTransfers_times() {
        return transfers_times;
    }

    public void setTransfers_times(String transfers_times) {
        this.transfers_times = transfers_times;
    }

    public String getCar_loc() {
        return car_loc;
    }

    public void setCar_loc(String car_loc) {
        this.car_loc = car_loc;
    }

    public String getCar_grade() {
        return car_grade;
    }

    public void setCar_grade(String car_grade) {
        this.car_grade = car_grade;
    }

    public String getCar_engine() {
        return car_engine;
    }

    public void setCar_engine(String car_engine) {
        this.car_engine = car_engine;
    }

    public String getCar_color() {
        return car_color;
    }

    public void setCar_color(String car_color) {
        this.car_color = car_color;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public String getPower_type() {
        return power_type;
    }

    public void setPower_type(String power_type) {
        this.power_type = power_type;
    }

    public String getCar_website() {
        return car_website;
    }

    public void setCar_website(String car_website) {
        this.car_website = car_website;
    }

    public String getRelease_time() {
        return release_time;
    }

    public void setRelease_time(String release_time) {
        this.release_time = release_time;
    }

    @Override
    public String toString() {
        return "Car{" +
                "cid='" + cid + '\'' +
                ", title='" + title + '\'' +
                ", car_photo='" + car_photo + '\'' +
                ", car_price=" + car_price +
                ", car_brand='" + car_brand + '\'' +
                ", car_type='" + car_type + '\'' +
                ", displayed_mileage=" + displayed_mileage +
                ", licensing_time=" + licensing_time +
                ", transmission=" + transmission +
                ", emissions=" + emissions +
                ", emission_standard='" + emission_standard + '\'' +
                ", annual_timeout='" + annual_timeout + '\'' +
                ", insurance_timeout='" + insurance_timeout + '\'' +
                ", quality_timeout='" + quality_timeout + '\'' +
                ", transfers_times='" + transfers_times + '\'' +
                ", car_loc='" + car_loc + '\'' +
                ", car_grade='" + car_grade + '\'' +
                ", car_engine='" + car_engine + '\'' +
                ", car_color='" + car_color + '\'' +
                ", fuel_type='" + fuel_type + '\'' +
                ", power_type='" + power_type + '\'' +
                ", release_time=" + release_time +
                '}';
    }
}
