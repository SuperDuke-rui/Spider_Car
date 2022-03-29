package com.spider.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
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
public class AppConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index.html").setViewName("index");

        //重置密码页
        registry.addViewController("/reset").setViewName("account/reset-password");
        // 登录页
        registry.addViewController("/login.html").setViewName("account/SpiderCar-login");
        // 注册页
        registry.addViewController("/register").setViewName("account/SpiderCar-register");
        // 用户信息界面（个人主页）
        registry.addViewController("/userProfile").setViewName("account/user-profile");
        // 二手车信息展示页
        registry.addViewController("/table_Datatable").setViewName("table-datatable");
        //二手车详细界面
        registry.addViewController("/carDetail").setViewName("car-details");
        //收藏页面
        registry.addViewController("/myStore").setViewName("myStore");
        //二手车信息对比页面
        registry.addViewController("/compare").setViewName("compare");

        //三个chart
        registry.addViewController("/Chart1").setViewName("charts/chartDemo");
        registry.addViewController("/Chart2").setViewName("charts/charts-chartjs");
        registry.addViewController("/Chart3").setViewName("charts/charts-highcharts");
    }

    // 登录拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/**").excludePathPatterns("/user/login","/user/register","/register","/login.html","/toRegister","/toLogin",
                        "/", "/welcome" ,"/css/*","/flags/**","/fonts/*","/images/**","/js/*","/plugins/**");//  "/*" :一级匹配； “/**”：多级匹配
    }

    /**
     * 分页插件
     * mybatis-plus自带的分页插件（新版本里才有，3.4版本）
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }
}
