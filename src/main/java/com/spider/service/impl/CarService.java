package com.spider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spider.mapper.CarMapper;
import com.spider.pojo.Car;
import com.spider.service.ICarService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/20 22:50
 */
@Service
public class CarService implements ICarService {

    @Resource
    private CarMapper carMapper;

    /**
     * 分页查询
     * @param pageNum 第几页
     * @param pageSize 每页多少条
     * @return
     */
    @Override
    public Map<String, Object> getCarMap(int pageNum, int pageSize) {

        Page<Car> pageInfo = new Page(pageNum, pageSize);
        Page<Car> iPage = carMapper.selectPage(pageInfo, null);
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("total_record", iPage.getTotal());
        pageMap.put("total_page", iPage.getPages());
        pageMap.put("current_data", iPage.getRecords());

        return pageMap;
    }

    /**
     * 分页查询（返回Page）
     * @param pageNum 第几页
     * @param pageSize 每页多少条
     * @return Page
     */
    @Override
    public Page<Car> getCarPage(int pageNum, int pageSize) {
        Page<Car> pageInfo = new Page<>(pageNum, pageSize);
        return carMapper.selectPage(pageInfo, null);
    }

    /**
     * 通过cid查找车辆信息
     * @param field 字段名
     * @param cid 字段值
     * @return
     */
    @Override
    public Car queryCar(String field, int cid) {
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(field, cid);
        return carMapper.selectOne(queryWrapper);
    }

    /**
     * 查询所有记录数
     * @return
     */
    @Override
    public int queryCount() {
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("uid");
        return carMapper.selectCount(queryWrapper);
    }


    /**
     * 更新数据（通过cid更新）
     * @param field 字段名
     * @param cid 字段值
     * @param car 要更新的类
     * @return int
     */
    @Override
    public int updateCar(String field, int cid, Car car) {
        UpdateWrapper<Car> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(field, cid);
        return carMapper.update(car, updateWrapper);
    }

    /**
     * 获取车辆类型
     * @return
     */
    @Override
    public List<String> getCarTypes() {
        return carMapper.getCarTypes();
    }

    /**
     * 获取车辆的动力类型：前置四驱
     * @return
     */
    @Override
    public List<String> getPowerTypes() {
        return carMapper.getPowerTypes();
    }

    /**
     * 获取车辆的变速箱类型：自动、手动
     * @return
     */
    @Override
    public List<String> getTransType() {
        return carMapper.getTransTypes();
    }

}
