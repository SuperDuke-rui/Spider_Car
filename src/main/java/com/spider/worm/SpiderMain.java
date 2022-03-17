package com.spider.worm;

import com.spider.pojo.Car;
import com.spider.utils.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.HttpCommandExecutor;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/8 16:18
 */
public class SpiderMain {

    //存放sessionId和serverURL
    static String sessionId = null;
    static String serverURL = null;

    public List<String> getPagesUrls() throws IOException {
        //1.二手车之家，默认地址暂为上海（shanghai），url为主页地址
        String url = "https://www.che168.com/shanghai/benchi/#pvareaid=104649";
        //2.1获取首页所有的a标签的href属性 (此处获得的是所有品牌二手车展示页面的urls)
        List<String> brand_urls = getUrlsFromIndex(url);
        //2.2继续通过获得的品牌展示urls获取到展示页中的每个二手车分页的所有地址信息car_pages_urls
        return getPagesUrlsFromBrandUrls(brand_urls);
    }

    /**
     * 爬取二手车数据
     * <p>
     * 废弃该方法****
     */
    public List<Car> getCars() throws IOException, InterruptedException, ParseException {
        //1.二手车之家，默认地址暂为上海（shanghai），url为主页地址
        String url = "https://www.che168.com/shanghai/benchi/#pvareaid=104649";
        //2.1获取首页所有的a标签的href属性 (此处获得的是所有品牌二手车展示页面的urls)
        List<String> brand_urls = getUrlsFromIndex(url);
//        List<String> brand_urls2 = new ArrayList<>();
//        brand_urls2.add("/shanghai/benchi/");

        //2.2继续通过获得的品牌展示urls获取到展示页中的每个二手车分页的所有地址信息car_pages_urls
//        List<String> car_pages_urls = getPagesUrlsFromBrandUrls(brand_urls);

        //2.3通过获取的所有品牌的所有分页的地址urls获取每个分页上所有单个二手车的urls并对其进行解析，返回CarList

//        return getDetailInfoFromPageUrls(car_pages_urls);

//        List<Car> carList = new ArrayList<>();
//        Car car = new Car();
//        car = getCarInfoFromUrl("https://www.che168.com/dealer/466131/41903402.html?pvareaid=100519&userpid=310000&usercid=310100&offertype=0&offertag=0&activitycartype=0#pos=12#page=1#rtype=10#isrecom=1#filter=29#module=10#refreshid=0#recomid=28814787#queryid=1647066209$11$f8c879cb-defc-4ebf-9d53-26af14781202$92989#cartype=30");
//
//        carList.add(car);
//        return carList;
        return null;
    }

    /**
     * 通过car_detail_url解析单个二手车详细页面信息并获取需要的信息
     *
     * @param car_detail_url
     * @return
     */
    private static Car getCarInfoFromUrl(String car_detail_url) throws IOException, ParseException, InterruptedException {
        if (null != car_detail_url) {
            //创建Car对象
            Car car = null;

            Document doc = handleUrls(car_detail_url);
            if (doc == null) {
                System.out.println("获取失败，继续下一个。。。");
                return null;
            }

            try {
                car = new Car();
                //1.车辆信息id
                //cid自增

                //2.车辆标题描述
                String title = doc.select(".car-brand-name").text();
                car.setTitle(title);

                //3.二手车图片
                String car_photo = "";
                try {
                    car_photo = "http:" + doc.select(".swiper-slide-active a img").first().attr("src");
                    car.setCar_photo(car_photo);
                } catch (NullPointerException e) {
                    System.out.println("未获取到图片");
                    car.setCar_photo("");
                }

                //4.车辆价格
                double car_price = 0;
                String string_price = doc.select("#overlayPrice").text();
//            System.out.println(string_price);
                String[] price_items = string_price.split(" ");
                car_price = Double.parseDouble(price_items[0].substring(1, price_items[0].length() - 1));
                car.setCar_price(car_price);
//            System.out.println(car_price);

                //5.车辆品牌
                String car_infos_string = doc.select(".bread-crumbs a").text();
                String[] car_base_infos = car_infos_string.split(" ");
                if (car_base_infos.length > 10) {
                    car.setCar_brand(car_base_infos[3]);
                } else if (car_base_infos.length == 10) {
                    car.setCar_brand(car_base_infos[2]);
                } else {
                    car.setCar_brand("-");
                }

                //6.车辆类型
                if (car_base_infos.length > 10) {
                    car.setCar_type(car_base_infos[4]);
                } else if (car_base_infos.length == 10) {
                    car.setCar_type(car_base_infos[3]);
                } else {
                    car.setCar_type("-");
                }

                //7.车辆表显里程
                String car_infos2_string = doc.select(".brand-unit-item h4").text();
                String[] car_base2_infos = car_infos2_string.split(" ");
                car.setDisplayed_mileage(car_base2_infos[0]);

                //8.车辆上牌时间
                car.setLicensing_time(car_base2_infos[1]);

                //9.车辆变速箱
                car.setTransmission(car_base2_infos[2]);

                //10.车辆排放量
                String emissions_string = car_base2_infos[4].substring(0, car_base2_infos[4].length() - 1);
                car.setEmissions(Double.parseDouble(emissions_string));

                //11.车辆排放标准
                car.setEmission_standard(car_base2_infos[6]);

                //12.车辆年检到期时间
                String car_document = doc.select(".all-basic-content li").text();
                String[] car_content = car_document.split(" ");
                String annual_timeout = car_content[10].substring(4);
                car.setAnnual_timeout(annual_timeout);

                //13.车辆保险到期时间
                String insurance_timeout = car_content[11].substring(4);
                car.setInsurance_timeout(insurance_timeout);

                //14.车辆质检到期时间
                String quality_timeout = car_content[12].substring(4);
                car.setQuality_timeout(quality_timeout);

                //15.车辆过户次数
                String transfers_times = car_content[14].substring(4);
                car.setTransfers_times(transfers_times);
//                System.out.println(transfers_times);

                //16.车辆所在地
//                if (car_base_infos.length > 10) {
//                    car.setCar_loc(car_base_infos[2]);
//                } else {
//                    car.setCar_loc(car_base_infos[1]);
//                }
                car.setCar_loc("上海");

                //17.车辆级别
                String car_grade = "";
                if (car_content.length > 28) {
                    car_grade = car_content[23].substring(4);
                } else if (car_content.length == 28) {
                    car_grade = car_content[22].substring(4);
                } else {
                    car_grade = "-";
                }
                car.setCar_grade(car_grade);
//                System.out.println(car_grade);

                //18.车辆发动机
                String car_engine = "";
                if (car_content.length > 28) {
                    car_engine = car_content[20].substring(1) + " " + car_content[21] + " " + car_content[22];
                } else if (car_content.length == 28) {
                    car_engine = car_content[20].substring(1) + " " + car_content[21];
                } else {
                    car_engine = "-";
                }

                car.setCar_engine(car_engine);
//                System.out.println(car_engine);

                //19.车辆颜色
                String car_color = "";
                if (car_content.length > 28) {
                    car_color = car_content[24].substring(4);
                } else if (car_content.length == 28) {
                    car_color = car_content[23].substring(4);
                } else {
                    car_color = "-";
                }
                car.setCar_color(car_color);
//                System.out.println(car_color);

                //20.燃油标号
                String fuel_type = "";
                if (car_content.length > 28) {
                    fuel_type = car_content[25].substring(4);
                } else if (car_content.length == 28) {
                    fuel_type = car_content[24].substring(4);
                } else {
                    fuel_type = "-";
                }

                car.setFuel_type(fuel_type);
//                System.out.println(fuel_type);

                //21.驱动方式
                String power_type = "";
                if (car_content.length > 28) {
                    power_type = car_content[26].substring(4);
                } else if (car_content.length == 28) {
                    power_type = car_content[25].substring(4);
                } else {
                    power_type = "-";
                }
                car.setPower_type(power_type);
//                System.out.println(power_type);

                //22.二手车网站
//                if (car_base_infos.length > 10) {
//                    car.setCar_website(car_base_infos[1]);
//                } else {
//                    car.setCar_website(car_base_infos[0]);
//                }
                car.setCar_website("二手车之家");

                //23.二手车信息发布时间
                String release_time = car_content[8].substring(4);
                car.setRelease_time(release_time);

                return car;
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("报错了，继续下一个。。。");
                return null;
            }

        }

        return null;
    }


    /**
     * 获取一个分页中的单个二手的车详细信息
     *
     * @param car_pages_url
     * @return List<Car> 一个分页的二手车列表
     * @throws IOException
     */
    public static List<Car> getDetailInfoFromPageUrl(String car_pages_url) throws IOException, ParseException, InterruptedException {

        //创建一个List存放每个二手车的Url
        List<Car> car_detail_infos = new ArrayList<>();

        Document doc = handleUrls(car_pages_url);

        Element element = doc.getElementById("goodStartSolrQuotePriceCore0");

        if (element != null) {
            Elements elements = element.getElementsByTag("a");

//            System.out.println(elements.size());

            for (Element elementEach : elements) {
                Car car = new Car();
                System.out.println("详情页面：" + elementEach.attr("href"));
                //使用获取到的单个url解析详情页面
                if (!elementEach.attr("href").startsWith("https://topicm")) {  //去除广告页面
                    if (elementEach.attr("href").startsWith("/dealer/")) {
                        car = getCarInfoFromUrl("https://www.che168.com" + elementEach.attr("href"));
                        //                    System.out.println("https://www.che168.com/" + elementEach.attr("href"));
                    } else {
                        car = getCarInfoFromUrl("https:" + elementEach.attr("href"));
                        //                    System.out.println("https:" + elementEach.attr("href"));
                    }
                    if (car != null) {
                        car_detail_infos.add(car);
                    }
                }
//                System.out.println(elementEach.attr("href"));
            }
        }

        return car_detail_infos;

    }

    /**
     * 继续通过获得的品牌展示urls获取到展示页中的每个二手车分页的所有地址信息car_pages_urls
     *
     * @param brand_urls
     * @return
     * @throws IOException
     */
    public static List<String> getPagesUrlsFromBrandUrls(List<String> brand_urls) throws IOException {
        //创建一个List
        List<String> car_pages_urls = new ArrayList<>();

//        //定义一个HashMap存放从品牌url中截取的品牌名，使用HashMap的key存放品牌名可以保证所有名称不重复出现
//        HashMap<String, String> brandMap = new HashMap<>();
//
//        // 获取到所有品牌名称
//        for (String brand_url : brand_urls) {
//            //截取链接中的品牌 如：https://www.che168.com/shanghai/aodi/#pvareaid=105866#listfilterstart 截取其中的 'aodi'
//            String[] brands = brand_url.split("/");
//
//            //获取到的品牌名称存入HashMap中
//            brandMap.put(brands[4], null);
//        }

        //拼接数据获取到所有分页url  https://www.che168.com/shanghai/ + benchi  + /a0_0msdgscncgpi1ltocsp + 2 + exx0/
        for (String brand_url : brand_urls) {

            System.out.println("当前获取的是：" + brand_url);

            //获取分页的数量
            int page_num = getPageNums("https://www.che168.com" + brand_url);

            //依次输出分页url
            for (int i = 1; i <= page_num; i++) {
                car_pages_urls.add("https://www.che168.com" + brand_url + "a0_0msdgscncgpi1ltocsp" + i + "exx0/");
                System.out.println("https://www.che168.com" + brand_url + "a0_0msdgscncgpi1ltocsp" + i + "exx0/");
            }

        }

        return car_pages_urls;
    }


    /**
     * 获取首页所有的a标签的href属性, 这里获取到的是所有品牌的展示页面的urls
     *
     * @param url
     * @return
     */

    private static List<String> getUrlsFromIndex(String url) {
        //创建一个List
        List<String> brand_urls = new ArrayList<>();

        //1.和二手车网站建立链接
        System.setProperty("webdriver.gecko.driver", "C:\\Program Files (x86)\\Mozilla Firefox\\geckodriver.exe");

        //初始化一个firefox浏览器实例 第一次通过new FireFoxDriver()启动浏览器
        FirefoxDriver driver = new FirefoxDriver();

        sessionId = String.valueOf(driver.getSessionId());

        serverURL = String.valueOf(((HttpCommandExecutor) driver.getCommandExecutor()).getAddressOfRemoteServer());


        //设置隐性等待时间
        driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);

        //get()打开一个站点
        driver.get(url);

        Document doc = Jsoup.parse(driver.getPageSource());

//        Elements elements = doc.select(".sub-element a");
        Element element = doc.getElementById("brandshow");

        assert element != null;
        Elements elements = element.getElementsByTag("a");
        System.out.println("共有" + elements.size() + "个分页");

        for (Element elementEach : elements) {
            if (!elementEach.attr("pingyin").isEmpty()) {
//                System.out.println("https://www.che168.com" + elementEach.attr("pingyin"));
                //elementEach.attr("pingyin")的值类型为：/shanghai/aodi/
                brand_urls.add(elementEach.attr("pingyin"));
            }
        }

        return brand_urls;
    }

    /**
     * 对大段重复代码进行抽取
     *
     * @param url
     * @return 返回获取到的正确的doc
     * @throws IOException
     */
    private static Document handleUrls(String url) throws IOException, InterruptedException {
        //复用WebDriver
        ReuseWebDriver driver = new ReuseWebDriver(serverURL, sessionId);

        driver.get(url);

        Document doc = Jsoup.parse(driver.getPageSource());

        //尝试获取错误页面的元素，若获取到错误页面，则重新设置ip
        String error_page = doc.select(".fail-page").text();
        while (!error_page.isEmpty()) {
            //重置代理ip
            HttpUtils.setProxyIp();

            Thread.sleep(2000);
            System.out.println("暂停两秒，再切换IP");

            //刷新当前网页
            driver.navigate().refresh();

            driver.get(url);

            doc = Jsoup.parse(driver.getPageSource());
            //再次获取错误页，如存在继续循环，知道获得正确的页面
            error_page = doc.select(".fail-page").text();
        }
        return doc;
    }

    /**
     * 通过url获得当前品牌最大的分页数 url样式为： "https://www.che168.com/shanghai/" + key + "/"
     * 创建一个String类型的数组 page_index 存放获取到的所有分页编号，包括“上一页”、最后的“下一页”，
     * 取倒数第二个即page_index[page_index.size()-2]为该页面分页的最大值
     *
     * @param brand_url
     * @return
     */
    private static int getPageNums(String brand_url) throws IOException {
        //如果只有一页或没有车源则返回默认的第一页
        int page_num = 1;

        // 1.和brand_url创建链接
        //如果某些内容是在加载页面后动态创建的，那么解析完整内容的最佳机会将是将Selenium与JSoup一起使用：
        //注意：因为你使用了selenium3+Firefox。在selenium3中，使用Firefox，需要添加驱动。
        //为了不重复打开浏览器，这里使用 CSDN 作者 汝贤 的方法复用WebDriver

        //复用WebDriver
        ReuseWebDriver driver = new ReuseWebDriver(serverURL, sessionId);

        //get()打开一个站点
        driver.get(brand_url);

        Document doc = Jsoup.parse(driver.getPageSource());

        String page_text = "";

        //如果ip被拦截重新设置代理IP
        //尝试获取错误页面的元素，若获取到错误页面，则重新设置ip
        String error_page = doc.select(".fail-page").text();
        while (!error_page.isEmpty()) {
            //重置代理ip
            HttpUtils.setProxyIp();

            driver.get(brand_url);

            doc = Jsoup.parse(driver.getPageSource());
            //再次获取错误页，如存在继续循环，知道获得正确的页面
            error_page = doc.select(".fail-page").text();
        }

        page_text = doc.select(".page a").text();

        String[] page_index = page_text.split(" ");

        System.out.println(Arrays.toString(page_index));

        // 至少有两页才有分页 即：“第一页”， “1”，“2”，“下一页”  page_index.length >= 4
        if (page_index.length >= 4) {
            System.out.println(page_index[page_index.length - 2]);
            String page_max = page_index[page_index.length - 2];
            page_num = Integer.parseInt(page_max);
        }


        return page_num;
    }

}
