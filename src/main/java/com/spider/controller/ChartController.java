package com.spider.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.spider.pojo.Car;
import com.spider.service.ICarService;
import com.spider.service.ISearchKeyService;
import com.spider.service.IStoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author wangrui
 * @Description 图表控制的controller
 * @date 2022/3/29 16:35
 */
@Controller
public class ChartController {
    @Resource
    private ISearchKeyService searchKeyService;

    @Resource
    private ICarService carService;

    @Resource
    private IStoreService storeService;

    /**
     * 热搜榜图表
     * @param model 用于传参
     * @return
     */
    @RequestMapping("/Chart1")
    public String chart01(Model model) {
        List<Map<String, Object>> searchKeysMap = searchKeyService.queryTopTen();

        model.addAttribute("topTen", searchKeysMap);
        return "charts/chart01";
    }

    /**
     * 二手车数据分析图表
     * @return
     */
    @RequestMapping("/Chart2")
    public String chart02(Model model) {
        //1.全局分析图
        List<Map<String, Object>> carBrandMap = carService.queryCarBrandAndNumber();
        //按数量排序
        sort(carBrandMap);
        //保存car_brand和num，直接用Car存放，不用再创建一个实体类
        List<Car> carList = new LinkedList<>();

        int totalNum = carService.queryCount();
        //保存全局分析图中需要的data
        JSONArray globalData = new JSONArray();
        //前9个品牌车辆总数
        int firstNineNum = 0;
        for (Map<String, Object> map : carBrandMap) {
            Number num = (Number) map.get("num");
            firstNineNum += num.intValue();

            JSONObject dataItem = new JSONObject();
            dataItem.put("value", num.intValue());
            dataItem.put("name", map.get("car_brand"));
            globalData.add(dataItem);

            Car car = new Car();
            car.setCarBrand((String) map.get("car_brand"));
            //用Car类中cid存放品牌车辆的记录数
            car.setCid(num.intValue());
            carList.add(car);
        }
        // JSONObject otherItem = new JSONObject();
        // otherItem.put("value", totalNum-firstNineNum);
        // otherItem.put("name", "其他");
        // globalData.add(otherItem);

        //2.收藏表品牌分布
        List<Map<String, Object>> storeBrandMap = storeService.queryBrandAndNumber();
        //排序
        sort(storeBrandMap);
        JSONArray brandData = new JSONArray();
        for (Map<String, Object> map : storeBrandMap) {
            Number num = (Number) map.get("num");

            JSONObject dataItem = new JSONObject();
            dataItem.put("value", num.intValue());
            if (map.get("car_brand").equals("-")) {
                dataItem.put("name", "其他");
            } else {
                dataItem.put("name", map.get("car_brand"));
            }
            brandData.add(dataItem);
        }
        //3.收藏表变速箱类型分布
        List<Map<String, Object>> storeTransMap = storeService.queryTransAndNumber();
        //排序
        sort(storeTransMap);
        JSONArray transData = new JSONArray();
        for (Map<String, Object> map : storeTransMap) {
            Number num = (Number) map.get("num");

            JSONObject dataItem = new JSONObject();
            dataItem.put("value", num.intValue());
            if (map.get("transmission").equals("-")) {
                dataItem.put("name", "其他");
            } else {
                dataItem.put("name", map.get("transmission"));
            }
            transData.add(dataItem);
        }
        //4.收藏表动力类型分布
        List<Map<String, Object>> storePowerMap = storeService.queryPowerAndNumber();
        //排序
        sort(storePowerMap);
        JSONArray powerData = new JSONArray();
        for (Map<String, Object> map : storePowerMap) {
            Number num = (Number) map.get("num");

            JSONObject dataItem = new JSONObject();
            dataItem.put("value", num.intValue());
            if (map.get("power_type").equals("-")) {
                dataItem.put("name", "其他");
            } else {
                dataItem.put("name", map.get("power_type"));
            }
            powerData.add(dataItem);
        }
        //5.收藏表车辆类型分布
        List<Map<String, Object>> storeTypeMap = storeService.queryTypeAndNumber();
        //排序
        sort(storeTypeMap);
        JSONArray typeData = new JSONArray();
        for (Map<String, Object> map : storeTypeMap) {
            Number num = (Number) map.get("num");

            JSONObject dataItem = new JSONObject();
            dataItem.put("value", num.intValue());
            if (map.get("car_grade").equals("-")) {
                dataItem.put("name", "其他");
            } else {
                dataItem.put("name", map.get("car_grade"));
            }
            typeData.add(dataItem);
        }

        //存放数量最多的二手车品牌、变速箱类型、动力类型、车辆类型
        List<String> maxItems = new LinkedList<>();
        if (storeBrandMap.size() != 0) {
            String maxBrand = (String) storeBrandMap.get(0).get("car_brand");
            maxItems.add(maxBrand);
        }
        if (storeTransMap.size() != 0) {
            String maxTrans = (String) storeTransMap.get(0).get("transmission");
            maxItems.add(maxTrans);
        }
        if (storePowerMap.size() != 0) {
            String maxPower = (String) storePowerMap.get(0).get("power_type");
            maxItems.add(maxPower);
        }
        if (storeTypeMap.size() != 0) {
            String maxType = (String) storeTypeMap.get(0).get("car_grade");
            maxItems.add(maxType);
        }

        model.addAttribute("GlobalData", globalData);
        model.addAttribute("BrandData", brandData);
        model.addAttribute("TransData", transData);
        model.addAttribute("PowerData", powerData);
        model.addAttribute("TypeData", typeData);
        model.addAttribute("CarList", carList);
        model.addAttribute("MaxItems", maxItems);

        return "charts/chart02";
    }

    /**
     * 对List<Map>按照num值进行排序
     * @param list
     * @return
     */
    public void sort(List<Map<String, Object>> list) {
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Number num1 = (Number) o1.get("num");
                Number num2 = (Number) o2.get("num");
                return num2.intValue() - num1.intValue();
            }
        });
    }
}
