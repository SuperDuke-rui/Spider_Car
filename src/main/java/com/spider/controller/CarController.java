package com.spider.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spider.pojo.Car;
import com.spider.service.ICarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/17 14:46
 */
@Controller
public class CarController {

    @Resource
    private ICarService carService;

    @RequestMapping("/carList")
    // @ResponseBody
    public String page(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "20") int pageSize, Model model) {
        // Map<String, Object> pageMap = carService.getCarMap(pageNum, pageSize);
        Page<Car> carPage = carService.getCarPage(pageNum, pageSize);
        //将map传到html中，进行取值并展示，展示页面做成卡片状
        model.addAttribute("carPage",carPage);
        return "cars-list";
    }


    /**
     * 从获取的title中提取车辆的品牌类型并更新到car_type中
     */
    @RequestMapping("/washData")
    public void updateBrandType(){
        int count = carService.queryCount();
        for(int i = 1; i < count; i++) {
            //1.通过cid获取车辆信息
            Car car = carService.queryCar("cid", i);
            //2.提取车辆的title
            String[] titleWords = (car.getTitle()).split(" ");
            System.out.println(titleWords[0]);

            car.setCarType(titleWords[0]);

            //3.更新车辆信息(通过cid)
            int result = carService.updateCar("cid", i, car);
            if (result >0){
                System.out.println("更新成功");
            } else {
                System.out.println("更新失败");
            }
        }
    }


}
