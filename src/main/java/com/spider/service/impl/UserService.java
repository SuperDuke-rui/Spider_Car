package com.spider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.spider.mapper.UserMapper;
import com.spider.pojo.User;
import com.spider.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/19 22:05
 */
@Service
public class UserService implements IUserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 查询用户
     * @param field 字段名
     * @param id id值
     * @return User
     */
    @Override
    public User queryUserById(String field, int id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(field, id);
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 使用String形字段查询用户
     * @param field 字段名
     * @param str 字段值
     * @return User
     */
    @Override
    public User queryUserByString(String field, String str) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(field, str);
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 用户更新（使用uid更新）
     * @param field 字段名
     * @param value 字段值
     * @param user 需要更新的用户类
     * @return int
     */
    @Override
    public int updateUser(String field, int value, User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(field, value);
        return userMapper.update(user, updateWrapper);
    }
}
