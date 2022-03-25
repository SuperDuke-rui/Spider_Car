package com.spider.service;

import com.spider.pojo.User;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/19 22:04
 */
public interface IUserService {
    /**
     * 使用uid查询用户
     * @param field 字段名
     * @param id 字段值
     * @return User
     */
    User queryUserById(String field, int id);

    /**
     * 使用String形字段查询用户
     * @param field 字段名
     * @param str 字段值
     * @return User
     */
    User queryUserByString(String field, String str);

    /**
     * 更新用户信息
     * @param field 字段名
     * @param value 字段值
     * @param user 需要更新的用户类
     * @return int
     */
    int updateUser(String field, int value, User user);

}
