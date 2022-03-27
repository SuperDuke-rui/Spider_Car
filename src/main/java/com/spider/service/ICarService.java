package com.spider.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spider.pojo.Car;

import java.util.List;
import java.util.Map;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/20 22:49
 */
public interface ICarService {
    /**
     * 分页查询
     * @param pageNum 第几页
     * @param pageSize 每页多少条
     * @return Page
     */
    Map<String, Object> getCarMap(int pageNum, int pageSize);

    /**
     * 分页查询，返回Page
     * @param pageNum 第几页
     * @param pageSize 每页多少条
     * @return Page
     */
    Page<Car> getCarPage(int pageNum, int pageSize);

    /**
     * 查找Car
     * @param field 字段名
     * @param cid 字段值
     * @return Car
     */
    Car queryCar(String field, int cid);

    /**
     * 获取Car的记录总数
     * @return
     */
    int queryCount();

    /**
     * 通过cid更新数据
     * @param field 字段名
     * @param cid 字段值
     * @param car 需要修改的Car
     * @return int
     */
    int updateCar(String field, int cid, Car car);

    /**
     * 获取车辆类型：大型车、中型车、小型车等
     * @return
     */
    List<String> getCarTypes();

    /**
     * 获取车辆的动力类型：前置四驱
     * @return
     */
    List<String> getPowerTypes();

    /**
     * 获取车辆的变速箱类型：自动、手动等
     * @return
     */
    List<String> getTransType();

    /**
     * 通过用户输入的关键词key搜索Car, 需要分页的
     * @param key 关键词
     * @param pageNum 当前页码
     * @param pageSize 页面大小
     * @return
     */
    Page<Car> queryByKey(String key, int pageNum, int pageSize);

    /**
     * 通过关键词查找符合条件的信息
     * @param key
     * @return
     */
    List<Car> queryCars(String key);

    /**
     * 多条件查询
     * @param carType 车辆类型
     * @param powerType 动力类型
     * @param transType 变速箱类型
     * @return
     */
    List<Car> queryByInterest(String carType, String powerType, String transType);
}