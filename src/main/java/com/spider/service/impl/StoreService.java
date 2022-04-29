package com.spider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spider.mapper.StoreMapper;
import com.spider.pojo.Store;
import com.spider.service.IStoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/27 22:32
 */
@Service
public class StoreService implements IStoreService {

    @Resource
    private StoreMapper storeMapper;

    /**
     * 保存信息到数据库
     * @param store store类
     * @return
     */
    @Override
    public int saveToStore(Store store) {
        return storeMapper.insert(store);
    }

    /**
     * 查询该信息是否已经收藏
     * @param uid 用户id
     * @param cid 车辆信息id
     * @return
     */
    @Override
    public Store queryStore(Integer uid, Integer cid) {
        return storeMapper.queryStore(uid, cid);
    }

    /**
     * 在数据库中搜索用户的收藏信息
     * @param uid 用户id
     * @param state 信息的状态
     * @return
     */
    @Override
    public List<Store> queryStoreList(Integer uid, Integer state) {
        return storeMapper.queryStoreList(uid, state);
    }

    /**
     * 查询用户的所有收藏信息，排序方式，首先按照状态降序排序，再按保存时间降序排序
     * @param uid 用户id
     * @return
     */
    @Override
    public List<Store> queryAllStores(Integer uid) {
        return storeMapper.queryAllStores(uid);
    }

    /**
     * 更新收藏信息状态
     * @param uid 用户id
     * @param cid 车辆信息id
     * @param state 更新状态为state
     * @return 更新结果
     */
    @Override
    public int updateState(Integer uid, Integer cid, Integer state, String newTime) {
        return storeMapper.updateState(uid, cid, state, newTime);
    }

    /**
     * 获取用户收藏表中的品牌分布
     * @return
     */
    @Override
    public List<Map<String, Object>> queryBrandAndNumber() {
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("car_brand", "COUNT(car_brand) num")
                .groupBy("car_brand")
                .orderByDesc("car_brand");
        return storeMapper.selectMaps(queryWrapper);
    }

    /**
     * 获取用户收藏表中的变速箱类型分布
     * @return
     */
    @Override
    public List<Map<String, Object>> queryTransAndNumber() {
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("transmission", "COUNT(transmission) num")
                .groupBy("transmission")
                .orderByDesc("transmission");
        return storeMapper.selectMaps(queryWrapper);
    }

    /**
     * 获取用户收藏表中的动力类型分布
     * @return
     */
    @Override
    public List<Map<String, Object>> queryPowerAndNumber() {
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("power_type", "COUNT(power_type) num")
                .groupBy("power_type")
                .orderByDesc("power_type");
        return storeMapper.selectMaps(queryWrapper);
    }

    /**
     * 获取用户收藏表中的车辆类型类型分布
     * @return
     */
    @Override
    public List<Map<String, Object>> queryTypeAndNumber() {
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("car_grade", "COUNT(car_grade) num")
                .groupBy("car_grade")
                .orderByDesc("car_grade");
        return storeMapper.selectMaps(queryWrapper);
    }
}
