package com.spider.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/5 9:35
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");

        //忘记密码页
        registry.addViewController("/forgot").setViewName("account/forgot-password");
        //重置密码页
        registry.addViewController("/reset").setViewName("account/reset-password");
        // 登录页
        registry.addViewController("/login.html").setViewName("account/SpiderCar-login");
        // 注册页
        registry.addViewController("/register").setViewName("account/SpiderCar-register");
        // 二手车信息展示页
        registry.addViewController("/carsList").setViewName("cars-list");
        // 用户信息界面（个人主页）
        registry.addViewController("/userProfile").setViewName("user-profile");
        // 二手车信息展示页
        registry.addViewController("/table_Datatable").setViewName("table-datatable");

        //欢迎页
        registry.addViewController("/welcome").setViewName("welcome");

        //三个chart
        registry.addViewController("/Chart1").setViewName("charts/charts-apex-chart");
        registry.addViewController("/Chart2").setViewName("charts/charts-chartjs");
        registry.addViewController("/Chart3").setViewName("charts/charts-highcharts");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/**").excludePathPatterns("/user/login","/login.html",
                        "/css/*","/flags/**","/fonts/*","/images/**","/js/*","/plugins/**");//  "/*" :一级匹配； “/**”：多级匹配
    }
}
