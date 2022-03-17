package com.spider.controller;

import com.spider.mapper.CarMapper;
import com.spider.mapper.PageUrlsMapper;
import com.spider.pojo.Car;
import com.spider.pojo.PageUrls;
import com.spider.worm.SpiderMain;
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
    @Resource
    private PageUrlsMapper pageUrlsMapper;

    @RequestMapping("/savePageUrls")
    @ResponseBody
    public void savePageUrlsToDb() throws IOException {

        SpiderMain spiderMain = new SpiderMain();

        //将获取到的分页urls存入数据库中
        List<String> pageUrlsList = spiderMain.getPagesUrls();

        PageUrls pageUrls = null;

        for (String s : pageUrlsList) {
            System.out.println(s);

            //将传入的String类型的数据封装成PageUrls类型进行插入
            pageUrls = new PageUrls();
            pageUrls.setPagesUrl(s);
            pageUrlsMapper.insert(pageUrls);
        }

        System.out.println("插入成功，共插入了" + pageUrlsList.size() + "条数据！");
    }

    @RequestMapping("/saveCars")
    @ResponseBody
    public void saveCarToDb() throws IOException, ParseException, InterruptedException {

        //开始爬取时间
        long startTime = new Date().getTime();

        SpiderMain spiderMain = new SpiderMain();

        //获取分页urls
        List<String> pageUrlsList = spiderMain.getPagesUrls();

        //创建一个List存放Car类
        List<Car> carList = null;

        //计数分页数
        int num = 1;
        //统计插入数据库的数据总数
        int sum = 0;

        //分别获取每个分页中的单个二手车信息并存储至数据库
        for (String eachPageUrl : pageUrlsList){

            System.out.println("当前爬取的是第"+ (num++) + "个分页，url为：" + eachPageUrl);
            carList = SpiderMain.getDetailInfoFromPageUrl(eachPageUrl);

            //统计插入数据量
            sum += carList.size();

            //存储该分页的数据到数据库中

            for (Car car : carList) {
                System.out.println(car);
                carMapper.insert(car);
            }

            System.out.println("插入成功，该分页插入了" + carList.size() + "条数据！");

        }


        long endTime = new Date().getTime();
        int hour = (int) ((endTime-startTime)/3600000);
        int minute = (int) (((endTime-startTime)-hour*3600000)/60000);
        int second = (int) ((((endTime-startTime)-hour*3600000)-minute*60000)/1000);
        System.out.println("插入结束，耗时" + hour + "时" + minute + "分" + second + "秒");
        System.out.println("插入完成，共插入" + sum + "条数据！");
    }

}
