package com.spider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spider.mapper.CarMapper;
import com.spider.pojo.Car;
import com.spider.service.ICarService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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

        Page<Car> pageInfo = new Page<>(pageNum, pageSize);
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

    /**
     * 通过用户输入的关键词key搜索Car,同样需要分页查询
     * @param key 关键词
     * @param pageNum 当前页码
     * @param pageSize 页面大小
     * @return
     */
    @Override
    public Page<Car> queryByKey(String key, int pageNum, int pageSize) {
        //获取查询的carList
        List<Car> carList = carMapper.queryByKey(key);
        //通过自定义函数对获取到的List进行分页
        return getPages(pageNum, pageSize, carList);
    }

    @Override
    public List<Car> queryCars(String key) {
        return carMapper.queryByKey(key);
    }

    @Override
    public List<Car> queryByInterest(String carType, String powerType, String transType) {
        return carMapper.queryByInterest(carType, powerType, transType);
    }

    /**
     * 自定义一个分页功能的函数
     * @param pageNum 当前页码
     * @param pageSize 页面大小
     * @param list 需要分页的List
     * @return Page<Car>
     */
    public Page<Car> getPages(int pageNum, int pageSize, List<Car> list) {
        Page<Car> carPage = new Page<>();

        int size = list.size();

        if (list.size() != 0) {

            //防止pageSize越界
            if (pageSize > size) {
                pageSize = size;
            }

            //求得最大页数，防止pageNum越界
            int maxPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;

            if (pageNum > maxPage) {
                pageNum = maxPage;
            }

            //当前第一条数据下标
            int curIds = pageNum > 1 ? (pageNum - 1) * pageSize : 0;

            List<Car> carList = new ArrayList<>();

            //将当前页的数据放进pageList中
            for (int i = 0; i < pageSize && curIds + i < size; i++) {
                carList.add(list.get(curIds + i));
            }

            carPage.setCurrent(pageNum).setSize(pageSize).setTotal(list.size()).setRecords(carList);

        } else {
            carPage.setCurrent(0).setSize(pageSize).setTotal(list.size()).setRecords(null);
        }

        return carPage;
    }


}
