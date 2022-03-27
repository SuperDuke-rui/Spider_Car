package com.spider.service.impl;

import com.spider.mapper.StoreMapper;
import com.spider.pojo.Store;
import com.spider.service.IStoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    public boolean queryStore(Integer uid, Integer cid) {
        Store store = storeMapper.queryStore(uid, cid);
        if (store != null){
            return true;
        } else {
            return false;
        }
    }
}
