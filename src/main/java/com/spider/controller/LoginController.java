package com.spider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/5 11:45
 */
@Controller
public class LoginController {
    @RequestMapping("/user/login")
    public String login(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         Model model, HttpSession session) {


        //具体的业务
        if (!StringUtils.isEmpty(username) && "123456".equals(password)) {
            session.setAttribute("loginUser", username);
            return "redirect:/index.html";
        } else{
            //告诉用户登录失败
            model.addAttribute("msg","用户名或者密码错误");
            return "account/SpiderCar-login";
        }

    }

    /**
     * 注销操作：清除session，重定向至登陆页面
     * @param session
     * @return
     */
    @RequestMapping("/user/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login.html";
    }
}
