package com.spider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spider.mapper.CarMapper;
import com.spider.pojo.Car;
import com.spider.service.ICarService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*");
        List<Car> carList = carMapper.selectList(queryWrapper);
        return getPages(pageNum, pageSize, carList);
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
        queryWrapper.isNotNull("cid");
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

    /**
     * 条件查找车辆（分页输出）
     * @param pageNum 当前页
     * @param pageSize 页面大小
     * @param brand 品牌
     * @param subBrand 子品牌
     * @param minPrice 价格下限
     * @param maxPrice 价格上限
     * @return
     */
    @Override
    public Page<Car> query(int pageNum, int pageSize, String brand, String subBrand, Double minPrice, Double maxPrice) {
        //获取查询的carList
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*");
        if (!Objects.equals(brand, "") && brand != null) {
            queryWrapper.eq("car_brand", brand);
        }
        if (!Objects.equals(subBrand, "") && subBrand != null) {
            queryWrapper.eq("car_type", subBrand);
        }
        if (minPrice != null) {
            queryWrapper.ge("car_price", minPrice);
        }
        if (maxPrice != null) {
            queryWrapper.le("car_price", maxPrice);
        }
        List<Car> carList = carMapper.selectList(queryWrapper);

        //通过自定义函数对获取到的List进行分页
        return getPages(pageNum, pageSize, carList);
    }

    /**
     * 通过关键词查找符合条件的信息
     * @param key
     * @return
     */
    @Override
    public List<Car> queryCars(String key) {
        return carMapper.queryByKey(key);
    }

    /**
     * 多条件查询
     * @param carType 车辆类型
     * @param powerType 动力类型
     * @param transType 变速箱类型
     * @return
     */
    @Override
    public List<Car> queryByInterest(String carType, String powerType, String transType) {
        //多添建查询
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*");
        if (!carType.equals("")) {
            queryWrapper.eq("car_grade", carType);
        }
        if (!powerType.equals("")) {
            queryWrapper.eq("power_type", powerType);
        }
        if (!transType.equals("")) {
            queryWrapper.eq("transmission", transType);
        }
        List<Car> carList = carMapper.selectList(queryWrapper);
        if (carList.size() == 0) {
            QueryWrapper<Car> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select("*");
            carList = carMapper.selectList(queryWrapper1);
        }
        return carList;
    }

    /**
     * 获取数据库中所有车辆的品牌
     * @return
     */
    @Override
    public List<Object> carBrands() {
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("car_brand")
                .groupBy("car_brand");
        return carMapper.selectObjs(queryWrapper);
    }

    /**
     * 查询车辆的子品牌
     * @param carBrand 车辆的品牌
     * @return
     */
    @Override
    public List<Object> carSubBrands(String carBrand) {
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("car_type")
                .eq("car_brand", carBrand)
                .groupBy("car_type");
        return carMapper.selectObjs(queryWrapper);
    }

    /**
     * 查询car_detail表中所有的车辆信息及数量
     * @return
     */
    @Override
    public List<Map<String, Object>> queryCarBrandAndNumber() {
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("car_brand", "COUNT(car_brand) num")
                .groupBy("car_brand")
                .orderByDesc("COUNT(car_brand)")
                .last("limit 15");
        return carMapper.selectMaps(queryWrapper);
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

            if (pageNum < 1) {
                pageNum = 1;
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
