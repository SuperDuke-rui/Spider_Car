package com.spider.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spider.pojo.Car;
import com.spider.pojo.SearchKey;
import com.spider.pojo.User;
import com.spider.service.ICarService;
import com.spider.service.ISearchKeyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/17 14:46
 */
@Controller
public class CarController {

    @Resource
    private ICarService carService;

    @Resource
    private ISearchKeyService searchKeyService;

    @RequestMapping("/carList")
    // @ResponseBody
    public String page(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "20") int pageSize,
                       Model model) {
        // Map<String, Object> pageMap = carService.getCarMap(pageNum, pageSize);
        Page<Car> carPage = carService.getCarPage(pageNum, pageSize);
        //将map传到html中，进行取值并展示，展示页面做成卡片状
        model.addAttribute("carPage",carPage);
        model.addAttribute("currentURL", "/carList?");

        //查询数据库中的车辆类型并传到前端，用于显示级联下拉框
        List<Object> brands = carService.carBrands();

        //定义一个Map用来存放车辆的品牌信息
        Map<String, List<Object>> brandMap = new LinkedHashMap<>();
        for (Object brand : brands) {
            List<Object> subBrands = carService.carSubBrands((String) brand);
            brandMap.put((String) brand, subBrands);
        }

        model.addAttribute("brandMap", brandMap);

        return "cars-list";
    }

    /**
     * 通过用户输入模糊查询
     * @param queryKey
     * @return
     */
    @RequestMapping("/user/queryByKey")
    public String queryByKey(@RequestParam String queryKey,
                             @RequestParam(defaultValue = "1") int pageNum,
                             @RequestParam(defaultValue = "20") int pageSize,
                             Model model){

        Page<Car> carPage = carService.queryByKey(queryKey, pageNum, pageSize);

        //保存查询的queryKey(关键词不为空)
        if (!Objects.equals(queryKey, "") && queryKey!=null) {
            if (carPage.getRecords()==null){
                queryKey = queryKey + "(无数据)";
            }
            SearchKey searchKey = new SearchKey();
            searchKey.setWords(queryKey);
            //获取当前时间
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = dateFormat.format(new Date());
            searchKey.setSearchTime(time);
            int result = searchKeyService.insertKey(searchKey);
            if (result > 0) {
                System.out.println("关键词插入成功");
            } else {
                System.out.println("关键词插入失败");
            }
        }

        //查询数据库中的车辆类型并传到前端，用于显示级联下拉框
        List<Object> brands = carService.carBrands();

        //定义一个Map用来存放车辆的品牌信息
        Map<String, List<Object>> brandMap = new LinkedHashMap<>();
        for (Object brand : brands) {
            List<Object> subBrands = carService.carSubBrands((String) brand);
            brandMap.put((String) brand, subBrands);
        }

        model.addAttribute("brandMap", brandMap);

        model.addAttribute("carPage", carPage);
        model.addAttribute("currentURL", "/user/queryByKey?queryKey="+queryKey+"&");

        return "cars-list";
    }

    /**
     * 点击进入详细页面
     * @param cid 通过列表页获取该二手车的cid，用cid查询详细信息
     * @param session 通过session向详细页面传递二手车信息
     * @return
     */
    @RequestMapping("/user/carDetailInfo")
    public String carDetail(Integer cid, HttpSession session) {

        //1.通过model传入car-details中
        Car car = carService.queryCar("cid", cid);
        session.setAttribute("carDetail", car);

        //2.1获取用户的信息
        User user = (User) session.getAttribute("loginUser");
        //2.2获取用户的偏好
        String interestInfo = user.getInterests();
        Map<String, String[]> map = analyseJson(interestInfo);
        //2.3通过用户的偏好查询车辆信息（在查出来的信息中随机获取12个通过session传入详细页面）
        String[] params = randomParam(map);
        //保证三个参数均不为空
        List<Car> carList;
        if (!Objects.equals(params[0], "") && !Objects.equals(params[1], "") && !Objects.equals(params[2], "")){
            carList = carService.queryByInterest(params[0], params[1], params[2]);
        } else if (!Objects.equals(params[0], "")) {
            carList = carService.queryCars(params[0]);
        } else if (!Objects.equals(params[1], "")) {
            carList = carService.queryCars(params[1]);
        } else if (!Objects.equals(params[2], "")) {
            carList = carService.queryCars(params[2]);
        } else {
            //如果全为空，那就随便推送点信息 (●^●)
            carList = carService.queryCars("");
        }
        //截取前12个数据
        if (carList.size() >= 12){
            session.setAttribute("recommend", carList.subList(0, 12));
        } else {
            session.setAttribute("recommend", carList);
        }

        return "redirect:/carDetail";
    }

    /**
     * 分析String型的json数据并返回
     */
    public Map<String, String[]> analyseJson(String jsonStr){
        JSONArray jsonArray = JSON.parseArray(jsonStr);

        //用Map返回结果
        Map<String, String[]> interestMap = new LinkedHashMap<>();

        //1.carType
        JSONArray carTypeArray = ((JSONObject)jsonArray.get(0)).getJSONArray("value");
        String[] carTypes = new String[carTypeArray.size()];
        int index = 0;
        for (int i = 0; i < carTypeArray.size(); i++) {
            Boolean value = (Boolean) ((JSONObject)carTypeArray.get(i)).get("value");
            if (value){
                String carType = (String)((JSONObject)carTypeArray.get(i)).get("key");
                carTypes[index] = carType;
                index ++;
            }
        }
        interestMap.put("CarType", carTypes);

        //2.PowerType
        JSONArray powerTypeArray = ((JSONObject)jsonArray.get(1)).getJSONArray("value");
        String[] powerTypes = new String[powerTypeArray.size()];
        index = 0;
        for (int i = 0; i < powerTypeArray.size(); i++) {
            Boolean value = (Boolean) ((JSONObject)powerTypeArray.get(i)).get("value");
            if (value){
                String powerType = (String)((JSONObject)powerTypeArray.get(i)).get("key");
                powerTypes[index] = powerType;
                index ++;
            }
        }
        interestMap.put("PowerType", powerTypes);

        //3.TransType
        JSONArray transTypeArray = ((JSONObject)jsonArray.get(2)).getJSONArray("value");
        String[] transTypes = new String[transTypeArray.size()];
        index = 0;
        for (int i = 0; i < transTypeArray.size(); i++) {
            Boolean value = (Boolean) ((JSONObject)transTypeArray.get(i)).get("value");
            if (value){
                String transType = (String)((JSONObject)transTypeArray.get(i)).get("key");
                transTypes[index] = transType;
                index ++;
            }
        }
        interestMap.put("TransType", transTypes);

        return interestMap;
    }

    /**
     * 传入一个Map<String, String[]>，随机返回三个参数，CarType、PowerType、TransType各一个
     */
    public String[] randomParam(Map<String, String[]> map) {
        String[] params = new String[3];
        String[] carTypes = map.get("CarType");
        //排除数组为空的情况
        if (countArray(carTypes) != 0){
            int randomNum = (int) Math.ceil(Math.random() * countArray(carTypes) - 1);
            params[0] = carTypes[randomNum];
        } else {
            params[0] = "";
        }

        String[] powerTypes = map.get("PowerType");
        if (countArray(powerTypes) != 0){
            int randomNum = (int) Math.ceil(Math.random() * countArray(powerTypes) - 1);
            params[1] = powerTypes[randomNum];
        } else {
            params[1] = "";
        }

        String[] transTypes = map.get("TransType");
        if (countArray(transTypes) != 0) {
            int randomNum = (int) Math.ceil(Math.random() * countArray(transTypes) - 1);
            params[2] = transTypes[randomNum];
        } else {
            params[2] = "";
        }

        return params;
    }

    /**
     * 计数数组中不为空的个数
     */
    public int countArray(String[] strArray) {
        int count = 0;
        for (int i = 0; i < strArray.length; i++) {
            //计数
            if (Objects.equals(strArray[i], "") || strArray[i] == null) {
                count ++;
            }
        }
        return count;
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

    /**
     * 条件查找车辆（分页输出）
     * @param pageNum 当前页
     * @param pageSize 页面大小
     * @param brand 品牌
     * @param subBrand 子品牌
     * @param minPrice 价格下限
     * @param maxPrice 价格上限
     * @param model 用于传值
     * @return
     */
    @RequestMapping("/user/query")
    public String query(@RequestParam(defaultValue = "1") int pageNum,
                        @RequestParam(defaultValue = "20") int pageSize,
                        String brand, String subBrand, Double minPrice, Double maxPrice, Model model) {
        Page<Car> carPage = carService.query(pageNum, pageSize, brand, subBrand, minPrice, maxPrice);
        //model传值
        model.addAttribute("carPage", carPage);
        model.addAttribute("currentURL", "/user/query?brand=" +
                (brand==null ? "" : brand) + "&subBrand=" + (subBrand==null ? "" : subBrand) +
                "&minPrice=" + (minPrice==null ? "" : minPrice) + "&maxPrice=" + (maxPrice==null ? "" : maxPrice) + "&");

        //查询数据库中的车辆类型并传到前端，用于显示级联下拉框
        List<Object> brands = carService.carBrands();

        //定义一个Map用来存放车辆的品牌信息
        Map<String, List<Object>> brandMap = new LinkedHashMap<>();
        for (Object brandOne : brands) {
            List<Object> subBrands = carService.carSubBrands((String) brandOne);
            brandMap.put((String) brandOne, subBrands);
        }

        model.addAttribute("brandMap", brandMap);

        return "cars-list";
    }

}
