package com.spider.controller;

import com.spider.mapper.CarMapper;
import com.spider.pojo.Car;
import com.spider.worm.SpiderMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/11 23:15
 */
@Controller
@RequestMapping("/spider")
public class SpiderController {
    @Resource
    private CarMapper carMapper;

    @RequestMapping("/saveCars")
    @ResponseBody
    public void saveCarToDb() throws IOException, ParseException, InterruptedException {

        //开始爬取时间
        long startTime = new Date().getTime();

        SpiderMain spiderMain = new SpiderMain();
        List<Car> carList = spiderMain.getCars();

        //爬取结束时间
        long middleTime = new Date().getTime();

        int hour = (int) ((middleTime-startTime)/3600000);
        int minute = (int) (((middleTime-startTime)-hour*3600000)/60000);
        int second = (int) ((((middleTime-startTime)-hour*3600000)-minute*60000)/1000);
        System.out.println("爬取结束，耗时" + hour + "时" + minute + "分" + second + "秒");

        System.out.println("=======================================================");

        System.out.println("开始插入数据库");

        for (int i = 0; i<carList.size(); i++){
            carMapper.insert(carList.get(i));
        }

        long endTime = new Date().getTime();
        hour = (int) ((endTime-middleTime)/3600000);
        minute = (int) (((endTime-middleTime)-hour*3600000)/60000);
        second = (int) ((((endTime-middleTime)-hour*3600000)-minute*60000)/1000);
        System.out.println("插入结束，耗时" + hour + "时" + minute + "分" + second + "秒");
        System.out.println("插入完成，共插入" + carList.size() + "条数据！");
    }
}
