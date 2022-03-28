package com.spider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spider.pojo.Store;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/27 22:30
 */
public interface StoreMapper extends BaseMapper<Store> {
    /*查询插入的信息是否已经存在*/
    @Select("SELECT * FROM user_store WHERE uid = #{uid} AND cid = #{cid}")
    Store queryStore(Integer uid, Integer cid);

    /*查询用户的收藏信息, 通过uid和信息状态进行查询*/
    @Select("SELECT * FROM user_store WHERE uid = #{uid} AND state = #{state} ORDER BY save_time DESC")
    List<Store> queryStoreList(Integer uid, Integer state);

    /*查询用户的所有收藏信息，排序方式，首先按照状态降序排序，再按保存时间降序排序*/
    @Select("SELECT * FROM user_store WHERE uid = #{uid} ORDER BY state DESC,save_time DESC")
    List<Store> queryAllStores(Integer uid);

    /*更新记录状态为state*/
    @Update("UPDATE user_store SET state = #{state} WHERE uid = #{uid} AND cid = #{cid}")
    int updateState(Integer uid, Integer cid, Integer state);
}
