package com.spider.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spider.mapper.UserMapper;
import com.spider.pojo.User;
import com.spider.service.IUserService;
import com.spider.service.impl.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/5 11:45
 */
@Controller
public class LoginController {

    @Resource
    private IUserService userService;

    @RequestMapping("/user/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model, HttpSession session) {

        //mybatis-plus通过实体类查询数据
        User selectUser = userService.queryUserByString("phone", username);

        System.out.println(selectUser);

        //具体的业务
//        if (!StringUtils.isEmpty(username) && "123456".equals(password)) {
        if (selectUser != null) {       //如果没查询到则不存在该用户
            if (selectUser.getPassword().equals(password)) {  //检查密码是否正确
                session.setAttribute("loginUser", selectUser);
                String role = (selectUser.getState() == 1) ? "管理员" : "普通用户";
                session.setAttribute("loginRole", role);
                return "redirect:/index.html";
            } else {
                //告诉用户登录失败
                model.addAttribute("msg", "用户名或者密码错误");
                return "account/SpiderCar-login";
            }
        } else {
            model.addAttribute("msg", "用户不存在，请先注册！");
            return "account/SpiderCar-login";
        }

    }

    /**
     * 跳转至注册页面
     *
     * @return
     */
    @RequestMapping("/toRegister")
    public String toRegister() {
        return "redirect:/register";
    }

    /**
     * 注销操作：清除session，重定向至登陆页面
     *
     * @param session
     * @return
     */
    @RequestMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login.html";
    }
}
