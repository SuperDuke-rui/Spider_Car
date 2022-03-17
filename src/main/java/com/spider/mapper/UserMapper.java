package com.spider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spider.pojo.User;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/15 16:50
 */
public interface UserMapper extends BaseMapper<User> {
    /*查询用户*/
    User selectOne(User user);
}
