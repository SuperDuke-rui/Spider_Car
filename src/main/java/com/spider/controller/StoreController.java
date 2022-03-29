package com.spider.controller;

import com.spider.pojo.Car;
import com.spider.pojo.Store;
import com.spider.pojo.User;
import com.spider.service.ICarService;
import com.spider.service.IStoreService;
import com.spider.utils.ExcelUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

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

    //车辆对比最大数量
    final int COMPARENUM = 4;

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
        Store queryStore = storeService.queryStore(user.getUid(), cid);
        if (queryStore != null){
            if (queryStore.getState() == 1) {
                model.addAttribute("msg", "该二手车信息已被收藏 ");
            } else {        //如果被收藏但，处于移除收藏状态，则修改状态为 1
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String newTime = dateFormat.format(new Date());
                int result = storeService.updateState(user.getUid(), cid, 1, newTime);
                System.out.println(result>0 ? "更新状态成功" : "更新状态失败");
                model.addAttribute("msg", "收藏成功");
            }
        } else {
            //保存二手车信息到数据库中
            Store store = new Store();
            setStoreValue(store, user, car, cid);
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
     * 抽取收藏的相同代码 此处抽取代码时出问题了，在抽取代码时需要考虑抽取的部分代码中
     * 是否有后续需要用的数据，如果有，抽取部分的返回值就不可为void
     * @param store
     * @param user
     * @param car
     * @param cid
     */
    private void setStoreValue(Store store, User user, Car car, Integer cid) {
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
    }

    /**
     * 在对比页面的收藏请求，返回的是对比页面
     * @param cid
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/compare/store")
    public String compareStore(Integer cid, Model model, HttpSession session) {
        //获取当前二手车信息
        Car car = carService.queryCar("cid", cid);
        //获取用户信息
        User user = (User)session.getAttribute("loginUser");
        //插入数据之前判断该条数据是否已经被收藏
        Store queryStore = storeService.queryStore(user.getUid(), cid);
        if (queryStore != null){
            if (queryStore.getState() == 1) {
                model.addAttribute("msg", "该二手车信息已被收藏");
            } else {        //如果被收藏但，处于移除收藏状态，则修改状态为 1
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String newTime = dateFormat.format(new Date());
                int result = storeService.updateState(user.getUid(), cid, 1, newTime);
                System.out.println(result>0 ? "更新状态成功" : "更新状态失败");
                model.addAttribute("msg", "收藏成功");
            }
        } else {
            //保存二手车信息到数据库中
            Store store = new Store();
            setStoreValue(store, user, car, cid);
            int result = storeService.saveToStore(store);
            if (result > 0) {
                model.addAttribute("msg", "收藏成功");
            } else {
                model.addAttribute("msg", "收藏失败，未成功插入数据");
            }
        }
        return comparePage(session, model);
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
     * 移除我的收藏，将该条记录修改状态为 0
     * @param cid
     * @param session
     * @return
     */
    @RequestMapping("/removeStore")
    public String deleteStore(Integer cid, HttpSession session) {
        //获取uid
        User user = (User) session.getAttribute("loginUser");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newTime = dateFormat.format(new Date());
        int result = storeService.updateState(user.getUid(), cid, 0, newTime);
        if (result > 0) {
            System.out.println("更新状态成功");
        } else {
            System.out.println("更新状态失败");
        }
        return "redirect:/user/myStore";
    }

    /**
     * 移入我的收藏，将该条记录修改状态为 1
     * @param cid
     * @param session
     * @return
     */
    @RequestMapping("/removeToStore")
    public String removeToStore(Integer cid, HttpSession session) {
        //获取uid
        User user = (User) session.getAttribute("loginUser");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newTime = dateFormat.format(new Date());
        int result = storeService.updateState(user.getUid(), cid, 1, newTime);
        if (result > 0) {
            System.out.println("更新状态成功");
        } else {
            System.out.println("更新状态失败");
        }
        return "redirect:/user/myStore";
    }

    /**
     * 将二手车加入对比
     * @param cid 车辆信息id
     * @param model
     * @param session 用session保存需要进行对比的二手车的cid
     * @return
     */
    @RequestMapping("/user/compare")
    public String carCompare(Integer cid, Model model, HttpSession session) {
        //最多同时比较5个信息
        int[] compareIds = new int[COMPARENUM];
        //判断是否存在compareIds的session
        if (session.getAttribute("compareIds") == null) {
            int idNum = countIds(compareIds); //计数,这里的计数一定为 0
            compareIds[idNum] = cid;
            session.setAttribute("compareIds", compareIds);
            model.addAttribute("msg", "添加成功！");
        } else {
            compareIds = (int[]) session.getAttribute("compareIds");
            int idNum = countIds(compareIds); //计数
            if (idNum >= COMPARENUM) {  //已经有5个cid了
                model.addAttribute("msg","最大对比个数为"+COMPARENUM+"个");
            } else {
                //判断该条信息是否已经存入
                if (haveCid(compareIds, cid)){
                    model.addAttribute("msg","该二手车已添加至对比");
                } else {
                    compareIds[idNum] = cid;
                    session.setAttribute("compareIds", compareIds);
                    model.addAttribute("msg", "添加成功！");
                }
            }
        }

        return "car-details";
    }

    /**
     * 移除对比表
     * @param cid
     * @return
     */
    @RequestMapping("/removeCompare")
    public String removeStore(Integer cid, HttpSession session) {
        int[] compareIds = (int[]) session.getAttribute("compareIds");
        for (int i = 0; i < countIds(compareIds); i++) {
            if (cid == compareIds[i]){
                //将删除位之后的每一个cid向前移动一位，最后一位置为0
                int j;
                for (j = i; j < countIds(compareIds)-1; j++) {
                    compareIds[j] = compareIds[j+1];
                }
                compareIds[j] = 0;
                break;
            }
        }
        session.setAttribute("compareIds", compareIds);
        return "redirect:/car/compare";
    }

    /**
     * 判断该cid是否已经存在
     * @param compareIds
     * @param cid
     * @return
     */
    public boolean haveCid(int[] compareIds, int cid) {
        for (int i = 0; i < compareIds.length; i++) {
            if (cid == compareIds[i]){
                return true;
            }
        }
        return false;
    }

    /**
     * 对int[]数组中不为空的数进行计数
     * @param compareIds 待计数的数组
     * @return 个数
     */
    public int countIds(int[] compareIds) {
        if (compareIds == null){
            return 0;
        }
        int num = 0;
        for (int i = 0; i < compareIds.length; i++) {
            if (compareIds[i] != 0) {
                num ++;
            }
        }
        return num;
    }

    /**
     * 二手车信息对比页
     * @param session
     * @param model 传输获取到的二手车信息
     * @return
     */
    @RequestMapping("/car/compare")
    public String comparePage(HttpSession session, Model model) {
        //获取session
        int[] compareIds = (int[]) session.getAttribute("compareIds");
        //通过session依次查询获取车辆信息
        List<Car> carList = new LinkedList<>();
        if (countIds(compareIds) != 0) {
            for (int i = 0; i < compareIds.length; i++) {
                if (compareIds[i] != 0) {
                    Car car = carService.queryCar("cid", compareIds[i]);
                    carList.add(car);
                }
            }
            List<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < countIds(compareIds); i++) {
                indexes.add(i+1);
            }
            model.addAttribute("compareCarsIndex", indexes);
        } else {
            carList = null;
        }
        model.addAttribute("compareCars", carList);
        return "compare";
    }

    /**
     * 下载用户的收藏信息
     * @param response
     * @param session
     * @throws IOException
     */
    @RequestMapping("/user/download")
    public void download(HttpServletResponse response, HttpSession session) throws IOException {
        User user = (User) session.getAttribute("loginUser");

        //填充project数据
        List<Store> storeList = storeService.queryStoreList(user.getUid(), 1);
        List<Map<String, Object>> list = createExcelRecord(storeList);
        String[] columnNames = {"序号","描述", "车辆照片", "价格", "品牌", "子品牌", "表显里程", "车辆上牌时间", "变速箱类型", "排量", "排放标准", "年检到期时间",
                "保险到期时间", "过户次数", "车辆所在地", "车辆级别", "发动机", "车辆颜色", "燃油标号", "驱动方式", "二手车网站", "二手车信息发布时间", "收藏时间"};
        String[] keys = {"id","title", "carPhoto", "carPrice", "carBrand", "carType", "displayedMileage", "licensingTime", "transmission", "emissions", "emissionStandard", "annualTimeout",
                "insuranceTimeout", "transfersTimes", "carLoc", "carGrade", "carEngine", "carColor", "fuelType", "powerType", "carWebsite", "releaseTime", "saveTime"};

        InputAndOutput(response, list, keys, columnNames);

    }

    /**
     * 下载用户的所有收藏信息（包括移除的部分信息）
     * 用户的所有收藏信息的排序方式，首先按照状态降序排序，再按保存时间降序排序
     * @param response
     * @param session
     * @throws IOException
     */
    @RequestMapping("/user/downloadAll")
    public void downloadAll(HttpServletResponse response, HttpSession session) throws IOException {
        User user = (User) session.getAttribute("loginUser");
        //填充project数据
        List<Store> storeList = storeService.queryAllStores(user.getUid());
        List<Map<String, Object>> list = createExcelRecord2(storeList);
        String[] columnNames = {"序号","描述", "车辆照片", "价格", "品牌", "子品牌", "表显里程", "车辆上牌时间", "变速箱类型", "排量", "排放标准", "年检到期时间",
                "保险到期时间", "过户次数", "车辆所在地", "车辆级别", "发动机", "车辆颜色", "燃油标号", "驱动方式", "二手车网站", "二手车信息发布时间", "收藏时间", "收藏状态"};
        String[] keys = {"id","title", "carPhoto", "carPrice", "carBrand", "carType", "displayedMileage", "licensingTime", "transmission", "emissions", "emissionStandard", "annualTimeout",
                "insuranceTimeout", "transfersTimes", "carLoc", "carGrade", "carEngine", "carColor", "fuelType", "powerType", "carWebsite", "releaseTime", "saveTime", "state"};

        InputAndOutput(response, list, keys, columnNames);
    }

    /**
     * 抽取大段相同代码
     * @param response
     * @param list
     * @param keys
     * @param columnNames
     * @throws IOException
     */
    public void InputAndOutput(HttpServletResponse response, List<Map<String, Object>> list, String[] keys, String[] columnNames) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ExcelUtil.createWorkBook(list, keys, columnNames).write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(content);

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String formatTime = simpleDateFormat.format(date);
        String fileName = "二手车信息表_" + formatTime;
        //设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xlsx").getBytes(), StandardCharsets.ISO_8859_1));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedOutputStream = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bufferedInputStream.read(buff, 0, buff.length))) {
                bufferedOutputStream.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bufferedInputStream != null){
                bufferedInputStream.close();
            }
            if (bufferedOutputStream != null) {
                bufferedOutputStream.close();
            }
        }
    }


    /**
     * 将storeList解析为MapList的形式
     * @param storeList 待解析的数据
     * @return
     */
    private List<Map<String, Object>> createExcelRecord(List<Store> storeList) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("sheetName", "sheet1");
        listMap.add(map);
        Store store;
        for (int i = 0; i < storeList.size(); i++) {
            store = storeList.get(i);
            Map<String, Object> mapValue = new LinkedHashMap<>();
            mapValue.put("id", i+1);
            putValues(mapValue, store);
            listMap.add(mapValue);
        }
        return listMap;
    }

    /**
     * 将storeList解析为MapList的形式
     * @param storeList 待解析的数据
     * @return
     */
    private List<Map<String, Object>> createExcelRecord2(List<Store> storeList) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("sheetName", "sheet1");
        listMap.add(map);
        Store store;
        for (int i = 0; i < storeList.size(); i++) {
            store = storeList.get(i);
            Map<String, Object> mapValue = new LinkedHashMap<>();
            mapValue.put("id", i+1);
            putValues(mapValue, store);
            mapValue.put("state", store.getState() == 1 ? "收藏中" : "已被移除");
            listMap.add(mapValue);
        }
        return listMap;
    }

    /**
     * 大段相同代码的抽取
     * @param mapValue
     * @param store
     */
    public void putValues(Map<String, Object> mapValue, Store store) {
        mapValue.put("title", store.getTitle());
        mapValue.put("carPhoto", store.getCarPhoto());
        mapValue.put("carPrice", store.getCarPrice() + "万元");
        mapValue.put("carBrand", store.getCarBrand());
        mapValue.put("carType", store.getCarType());
        mapValue.put("displayedMileage", store.getDisplayedMileage());
        mapValue.put("licensingTime", store.getLicensingTime());
        mapValue.put("transmission", store.getTransmission());
        mapValue.put("emissions", store.getEmissions() + "升");
        mapValue.put("emissionStandard", store.getEmissionStandard());
        mapValue.put("annualTimeout", store.getAnnualTimeout());
        mapValue.put("insuranceTimeout", store.getInsuranceTimeout());
        mapValue.put("transfersTimes", store.getTransfersTimes());
        mapValue.put("carLoc", store.getCarLoc());
        mapValue.put("carGrade", store.getCarGrade());
        mapValue.put("carEngine", store.getCarEngine());
        mapValue.put("carColor", store.getCarColor());
        mapValue.put("fuelType", store.getFuelType());
        mapValue.put("powerType", store.getPowerType());
        mapValue.put("carWebsite", store.getCarWebsite());
        mapValue.put("releaseTime", store.getReleaseTime());
        mapValue.put("saveTime", store.getSaveTime());
    }
}
