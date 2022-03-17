package com.spider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author wangrui
 * @Description 欢迎页的Controller
 * @date 2022/3/15 13:26
 */
@Controller
public class WelcomeController {
    /**
     * 进入默认欢迎页
     * @return
     */
    @RequestMapping("/")
    public String welcome(){
        return "welcome";
    }

    /**
     * 跳转至登录页面
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin() {
        return "redirect:/login.html";
    }
}
