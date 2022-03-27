package com.spider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spider.pojo.Store;
import org.apache.ibatis.annotations.Select;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/27 22:30
 */
public interface StoreMapper extends BaseMapper<Store> {
    /*查询插入的信息是否已经存在*/
    @Select("SELECT * FROM user_store WHERE uid = #{uid} AND cid = #{cid}")
    Store queryStore(Integer uid, Integer cid);
}
