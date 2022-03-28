package com.spider.controller;

import com.spider.pojo.Car;
import com.spider.pojo.Store;
import com.spider.pojo.User;
import com.spider.service.ICarService;
import com.spider.service.IStoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/27 22:33
 */
@Controller
public class StoreController {

    @Resource
    private ICarService carService;

    @Resource
    private IStoreService storeService;

    /**
     * 用户收藏当前的二手车信息
     * @param cid 二手车的id
     * @param model 用于回显数据
     * @return
     */
    @RequestMapping("/user/store")
    public String carStore(Integer cid, Model model, HttpSession session) {
        //获取当前二手车信息
        Car car = carService.queryCar("cid", cid);
        //获取用户信息
        User user = (User)session.getAttribute("loginUser");
        //插入数据之前判断该条数据是否已经被收藏
        if (storeService.queryStore(user.getUid(), cid)){
            model.addAttribute("msg", "该二手车信息已被收藏");
        } else {
            //保存二手车信息到数据库中
            Store store = new Store();
            store.setUid(user.getUid());
            store.setCid(cid);
            store.setTitle(car.getTitle());
            store.setCarPhoto(car.getCarPhoto());
            store.setCarPrice(car.getCarPrice());
            store.setCarBrand(car.getCarBrand());
            store.setCarType(car.getCarType());
            store.setDisplayedMileage(car.getDisplayedMileage());
            store.setLicensingTime(car.getLicensingTime());
            store.setTransmission(car.getTransmission());
            store.setEmissions(car.getEmissions());
            store.setEmissionStandard(car.getEmissionStandard());
            store.setAnnualTimeout(car.getAnnualTimeout());
            store.setInsuranceTimeout(car.getInsuranceTimeout());
            store.setTransfersTimes(car.getTransfersTimes());
            store.setCarLoc(car.getCarLoc());
            store.setCarGrade(car.getCarGrade());
            store.setCarEngine(car.getCarEngine());
            store.setCarColor(car.getCarColor());
            store.setFuelType(car.getFuelType());
            store.setPowerType(car.getPowerType());
            store.setCarWebsite(car.getCarWebsite());
            store.setReleaseTime(car.getReleaseTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            store.setSaveTime(dateFormat.format(new Date()));
            store.setState(1);
            int result = storeService.saveToStore(store);
            if (result > 0) {
                model.addAttribute("msg", "收藏成功");
            } else {
                model.addAttribute("msg", "收藏失败，未成功插入数据");
            }
        }

        return "car-details";
    }

    /**
     * 用户收藏页，从数据库中查询数据并展示到页面上
     * @return
     */
    @RequestMapping("/user/myStore")
    public String myStore(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loginUser");
        //获取正常状态的收藏信息
        List<Store> storeList = storeService.queryStoreList(user.getUid(), 1);
        //获取被逻辑删除的收藏信息
        List<Store> storeListDel = storeService.queryStoreList(user.getUid(), 0);

        model.addAttribute("storeList", storeList);
        model.addAttribute("storeListDel", storeListDel);

        return "/myStore";
    }

    /**
     * 移除我的收藏，将该条记录修改状态为0
     * @param cid
     * @param session
     * @return
     */
    @RequestMapping("/deleteStore")
    public String deleteStore(Integer cid, HttpSession session) {
        //获取uid
        User user = (User) session.getAttribute("loginUser");
        int result = storeService.updateState(user.getUid(), cid, 0);
        if (result > 0) {
            System.out.println("更新状态成功");
        } else {
            System.out.println("更新状态失败");
        }
        return "redirect:/user/myStore";
    }

    /**
     * 移入我的收藏，将该条记录修改状态为1
     * @param cid
     * @param session
     * @return
     */
    @RequestMapping("/removeToStore")
    public String removeToStore(Integer cid, HttpSession session) {
        //获取uid
        User user = (User) session.getAttribute("loginUser");
        int result = storeService.updateState(user.getUid(), cid, 1);
        if (result > 0) {
            System.out.println("更新状态成功");
        } else {
            System.out.println("更新状态失败");
        }
        return "redirect:/user/myStore";
    }
}
