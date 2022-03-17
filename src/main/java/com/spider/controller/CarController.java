package com.spider.controller;

import com.spider.mapper.CarMapper;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/17 14:46
 */
@Controller
public class CarController {

    @Resource
    private CarMapper carMapper;


}
