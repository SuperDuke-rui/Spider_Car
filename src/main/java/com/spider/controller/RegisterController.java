package com.spider.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spider.mapper.UserMapper;
import com.spider.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @Author wangrui
 * @Description 注册功能
 * @date 2022/3/14 23:30
 */
@Controller
public class RegisterController {

    @Resource
    private UserMapper userMapper;

    /**
     * 用户注册
     * @return
     */
    @RequestMapping("/user/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("phone") String phone,
                           @RequestParam("email") String email,
                           @RequestParam("location") String location,
                           Model model) {

        //查询数据
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        User selectUser = userMapper.selectOne(queryWrapper);
        System.out.println(selectUser);

        //如果查询到，则回显消息，否则将数据插入
        if (selectUser != null) {
            model.addAttribute("msg","账号已被注册");
        } else {
            //先查询该用户名是否已经注册
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setPhone(phone);
            user.setEmail(email);
            user.setLocation(location);
            //默认注册用户为普通用户
            user.setState(0);

            //设置默认头像
            user.setUserPhoto("/images/user-images/user-images-1.png");

            //向数据库中插入用户信息
            userMapper.insert(user);
            model.addAttribute("msg", "注册成功！");
        }
        return "account/SpiderCar-register";
    }
}
