package com.spider.worm;

import com.spider.pojo.Car;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author wangrui
 * @Description 测试Jsoup类
 * @date 2022/3/8 15:28
 */
public class JsoupTest {

    @Test
    public void test1() throws IOException {
        //Jsoup 入门
        Document doc = Jsoup.connect("http://www.itcast.cn/").get();

        //获取数据
        Elements elements = doc.getElementsByTag("a");
        for (Element element : elements) {
            System.out.println(element);
        }
    }

    @Test
    public void test2() throws IOException {
        //1.使用Jsoup建立连接
        Connection conn = Jsoup.connect("http://www.itcast.cn/");

        //2.设置请求连接，反爬虫
        conn.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36 Edg/99.0.1150.30");
        conn.timeout(5000);

        //3.发送请求
        Document doc = conn.get();
//        String title = doc.title();

//        System.out.println(title);

        //4.使用选择器选择页面数据
        //4.1选中含有href属性的a标签
//        Elements elements = doc.select("a[href]");
//        for (Element element : elements) {
//            System.out.println(element);
//        }

        //4.2使用选择器获取图片
        Elements elements = doc.select("img[src$=.png]");
        for (Element element : elements){
            System.out.println("http://www.itcast.cn/" + element.attr("src"));
        }

    }

    @Test
    public void test3() throws IOException, ParseException, InterruptedException {
        List<Car> carList = new ArrayList<>();
        String brand = "奥迪";
        carList = SpiderMain.getDetailInfoFromPageUrl("https://www.che168.com/shanghai/aodi/a0_0msdgscncgpi1ltocsp1exx0/", brand);
    }

    @Test
    public void test4() throws IOException {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("奥迪","/shanghai/aodi/");
        Map<String, List<String>> map1 = SpiderMain.getPagesUrlsFromBrandUrls(map);

    }

    /**
     * 定时任务：删除过期的数据
     */
    @Test
    public void deleteOutTimeData() {
        //时间格式定义
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //首先获取当前时间-nowDate
        // String nowDate = format.format(new Date());
        // System.out.println(nowDate);
        //获取30天前的时间日期-outTime
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -49);
        String outDate = format.format(calendar.getTime());
        System.out.println(outDate);
    }

}
