package com.spider.controller;

import com.spider.mapper.CarMapper;
import com.spider.mapper.PageUrlsMapper;
import com.spider.pojo.Car;
import com.spider.pojo.PageUrls;
import com.spider.service.IPageUrlsService;
import com.spider.worm.SpiderMain;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private IPageUrlsService pageUrlsService;

    @RequestMapping("/spiderControl")
    public String goToSpiderController(Model model) {
        //获取page_urls表格中所有的品牌名
        List<Object> brands = pageUrlsService.brandsFromPageUrls();
        // System.out.println(brands);
        model.addAttribute("pageUrlBrand", brands);
        return "spider-control";
    }

    /**
     * 获取到展示页中的每个二手车分页的所有地址信息car_pages_urls
     * @throws IOException
     */
    @RequestMapping("/savePageUrls")
    @ResponseBody
    public void savePageUrlsToDb() throws IOException {

        SpiderMain spiderMain = new SpiderMain();
        String url = "https://www.che168.com/shanghai/benchi/#pvareaid=104649";

        //将获取到的分页urls存入数据库中
        Map<String, List<String>> pageUrlsListMap = spiderMain.getPagesUrls(url);

        PageUrls pageUrls;

        int sum = 0;

        Iterator<Map.Entry<String, List<String>>> iterator = pageUrlsListMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, List<String>> entry = iterator.next();

            List<String> list = entry.getValue();
            for (String item : list) {
                //将传入的String类型的数据封装成PageUrls类型进行插入
                pageUrls = new PageUrls();
                pageUrls.setBrand(entry.getKey());
                pageUrls.setPagesUrl(item);
                pageUrlsService.insert(pageUrls);
            }
            sum += list.size();
        }

        System.out.println("插入成功，共插入了" + sum + "条数据！");
    }

    /**
     * 定时任务：每天凌晨两点爬取数据
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    @Scheduled(cron = "0 34 14 * * ?")
    public void schedule() throws IOException, ParseException, InterruptedException {
        saveCarToDb(null);
    }

    /**
     * 获取所有的二手车信息
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    @RequestMapping("/getCars")
    public String saveCarToDb(Model model)
            throws IOException, ParseException, InterruptedException {

        //开始爬取时间
        long startTime = new Date().getTime();

        List<PageUrls> urls = pageUrlsService.getUrls();

        //创建一个List存放Car类
        List<Car> carList;

        //计数分页数
        int num = 1;
        //统计插入数据库的数据总数
        int sum = 0;

        //分别获取每个分页中的单个二手车信息并存储至数据库
        for (PageUrls eachPageUrl : urls){

            System.out.println("当前爬取的是第"+ (num++) + "个分页，url为：" + eachPageUrl.getPagesUrl());
            carList = SpiderMain.getDetailInfoFromPageUrl(eachPageUrl.getPagesUrl(), eachPageUrl.getBrand());

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

        model.addAttribute("msg", "爬取和插入数据库共耗时" + hour + "时" + minute + "分" + second + "秒，共插入" + sum + "条数据！" );

        return "spider-control";
    }

    /**
     * 获取单个品牌的二手车信息
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    @RequestMapping("/getOnePageCars")
    public String getOnePageCars(@RequestParam String brand, Model model)
            throws IOException, ParseException, InterruptedException {

        //开始爬取时间
        long startTime = new Date().getTime();

        //创建一个List存放Car类
        List<Car> carList;

        //计数爬取到的二手车数量
        int sum = 0;

        List<PageUrls> pageUrls = pageUrlsService.getUrlsOfBrand(brand);

        for (PageUrls pageUrl : pageUrls) {
            carList = SpiderMain.getDetailInfoFromPageUrl(pageUrl.getPagesUrl(),pageUrl.getBrand());
            //存储该品牌的数据到数据库中
            for (Car car : carList) {
                System.out.println(car);
                carMapper.insert(car);
            }

            sum += carList.size();
        }

        long endTime = new Date().getTime();
        int hour = (int) ((endTime - startTime) / 3600000);
        int minute = (int) (((endTime-startTime)-hour*3600000)/60000);
        int second = (int) ((((endTime-startTime)-hour*3600000)-minute*60000)/1000);
        System.out.println("耗时" + hour + "时" + minute + "分" + second + "秒");
        System.out.println("插入完成，共插入" + sum + "条数据！");

        model.addAttribute("msg", "爬取和插入数据库共耗时" + hour + "时" + minute + "分" + second + "秒，共插入" + sum + "条数据！" );

        return "spider-control";
    }

}
