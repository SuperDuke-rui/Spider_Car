package com.spider.service;

import com.spider.pojo.Store;

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
}
