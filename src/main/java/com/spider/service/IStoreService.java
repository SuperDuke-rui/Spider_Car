package com.spider.service;

import com.spider.pojo.Store;

import java.util.List;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/27 22:32
 */
public interface IStoreService {
    /**
     * 保存信息到数据库
     * @param store store类
     * @return 返回插入结果
     */
    int saveToStore(Store store);

    /**
     * 查询数据库中是否存储了相同的信息
     * @param uid 用户id
     * @param cid 车辆信息id
     * @return 返回查询结果
     */
    boolean queryStore(Integer uid, Integer cid);

    /**
     * 在数据库中搜索用户的收藏信息
     * @param uid 用户id
     * @param state 信息的状态
     * @return List
     */
    List<Store> queryStoreList(Integer uid, Integer state);

    /**
     * 查询用户的所有收藏信息，排序方式，首先按照状态降序排序，再按保存时间降序排序
     * @param uid 用户id
     * @return
     */
    List<Store> queryAllStores(Integer uid);

    /**
     * 更新收藏信息状态
     * @param uid 用户id
     * @param cid 车辆信息id
     * @param state 更新状态为state
     * @return 更新结果
     */
    int updateState(Integer uid, Integer cid, Integer state);
}
