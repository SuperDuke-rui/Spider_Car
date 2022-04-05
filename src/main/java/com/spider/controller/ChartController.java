package com.spider.controller;

import com.spider.service.ISearchKeyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author wangrui
 * @Description 图表控制的controller
 * @date 2022/3/29 16:35
 */
@Controller
public class ChartController {
    @Resource
    private ISearchKeyService searchKeyService;

    @RequestMapping("/Chart1")
    public String chart01(Model model) {
        List<Map<String, Object>> searchKeysMap = searchKeyService.queryTopTen();

        model.addAttribute("topTen", searchKeysMap);
        return "charts/chart01";
    }
}
