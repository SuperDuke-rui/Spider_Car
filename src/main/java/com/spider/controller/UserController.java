package com.spider.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.spider.pojo.User;
import com.spider.service.ICarService;
import com.spider.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/17 17:22
 */
@Controller
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private ICarService carService;

    /**
     * 更新个人信息，可修改的信息包括：用户名（可被用来登录），手机号码，邮件，所在地，头像（单独更新）
     *
     * @return
     */
    @RequestMapping("/user/update")
    public String updateUserInfo(@RequestParam String username,
                                 @RequestParam String phone,
                                 @RequestParam String email,
                                 @RequestParam String location,
                                 Model model, HttpSession session) {

        User user = (User) session.getAttribute("loginUser");

        System.out.println("loginUser=" + user);

        //更新信息
        user.setUsername(username);
        user.setPhone(phone);
        user.setEmail(email);
        user.setLocation(location);

        //通过登录账号查询信息
        int result = userService.updateUser("uid", user.getUid(), user);

        if (result > 0) {
            model.addAttribute("msg", "更新成功");
            System.out.println("更新成功");
            session.setAttribute("loginUser", user);
        } else {
            model.addAttribute("msg", "更新失败");
            System.out.println("更新失败");
        }
        return "account/user-profile";
    }

    /**
     * 重置密码
     * @return
     */
    @RequestMapping("/user/reset")
    public String reset(@RequestParam String oldPassword,
                        @RequestParam String newPassword,
                        Model model, HttpSession session) {

        User user = (User) session.getAttribute("loginUser");

        //先查询原密码是否正确
        User selectUser = userService.queryUserById("uid", user.getUid());
        if (!(selectUser.getPassword().equals(oldPassword))) {
            //如果原密码输入错误则更新失败
            model.addAttribute("msg", "原密码错误");
        } else {
            user.setPassword(newPassword);

            int result = userService.updateUser("uid", user.getUid(), user);

            if (result > 0) {
                //清除session
                session.invalidate();
                model.addAttribute("msg", "密码更新成功，请重新登录");
            } else {
                model.addAttribute("msg", "密码更新失败");
            }
        }
        return "account/reset-password";

    }

    /**
     * 重置密码之后需要清除session再返回登录页面
     * @param session
     * @return
     */
    @RequestMapping("/BackToLogin")
    public String backToLogin(HttpSession session) {
        //清除session
        session.invalidate();
        return "redirect:/login.html";
    }

    /**
     * 更换头像
     * @param srcString
     * @param session
     * @return
     */
    @RequestMapping("/changeImg")
    public String uploadImg(@RequestParam String srcString, HttpSession session) {
        // System.out.println("strString=" + srcString);
        User user = (User) session.getAttribute("loginUser");
        user.setUserPhoto(srcString);

        int result = userService.updateUser("uid", user.getUid(), user);
        if (result > 0) {
            System.out.println("更新成功");
            session.setAttribute("loginUser", user);
        } else {
            System.out.println("更新失败");
        }

        return "account/user-profile";
    }

    /**
     * 用户个人主页
     * @param session
     * @return
     */
    @RequestMapping("/user/userProfile")
    public String getTags(HttpSession session) {

        //在此处分析session中的json数据并返回到html中，，，注册时默认给用户一个全为false的偏好
        //下面的都可以删除

        //1.首先获取当前用户的偏好信息，如果是空的，则先通过搜索车辆信息构造json数据保存到数据库并赋值到session中
        User user = (User) session.getAttribute("loginUser");
        //1.1创建json数组
        JSONArray jsonArray = new JSONArray();
        if (user.getInterests() == null || Objects.equals(user.getInterests(), "")) {

            //1.1.1获取车辆类型
            List<String> carTypes = carService.getCarTypes();
            carTypes.remove("-");
            JSONObject jsonObject = new JSONObject();

            JSONArray carTypeArray = new JSONArray();
            for (String carType: carTypes) {
                JSONObject carTypeObject = new JSONObject();

                carTypeObject.put("key", carType);
                carTypeObject.put("value", false);

                carTypeArray.add(carTypeObject);
            }
            jsonObject.put("key", "CarType");
            jsonObject.put("value", carTypeArray);
            jsonArray.add(jsonObject);

            //1.1.2获取车辆动力类型
            List<String> powerTypes = carService.getPowerTypes();
            powerTypes.remove("-");
            JSONObject jsonObject2 = new JSONObject();

            JSONArray powerTypeArray = new JSONArray();
            for (String powerType : powerTypes) {
                JSONObject powerTypeObject = new JSONObject();
                powerTypeObject.put("key", powerType);
                powerTypeObject.put("value", false);
                powerTypeArray.add(powerTypeObject);
            }
            jsonObject2.put("key", "PowerType");
            jsonObject2.put("value", powerTypeArray);
            jsonArray.add(jsonObject2);

            //1.1.3获取车辆变速箱类型
            List<String> transTypes = carService.getTransType();
            JSONObject jsonObject3 = new JSONObject();

            JSONArray transTypeArray = new JSONArray();
            for (String transType :
                    transTypes) {
                JSONObject transTypeObject = new JSONObject();
                transTypeObject.put("key", transType);
                transTypeObject.put("value", false);
                transTypeArray.add(transTypeObject);
            }
            jsonObject3.put("key", "TransType");
            jsonObject3.put("value", transTypeArray);
            jsonArray.add(jsonObject3);

            String jsonOutput = jsonArray.toJSONString();

            System.out.println(jsonOutput);

            user.setInterests(jsonOutput);
            int result = userService.updateUser("uid", user.getUid(), user);
            if (result > 0) {
                System.out.println("偏好成功插入");
            } else {
                System.out.println("偏好插入失败");
            }
            // model.addAttribute("interests",jsonArray);
            setInterestModel(user, session);
        }
        else {    //2.如果用户的偏好信息不为空，则通过解析json数据获取选项

            //设置用户偏好model
            setInterestModel(user, session);

        }

        return "account/user-profile";
    }

    /**
     * 保存用户偏好
     * @param interestStr 用户偏好json字符串
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/saveTags")
    public String saveTags(@RequestParam String interestStr,
                           HttpSession session, Model model) {
        System.out.println(interestStr);
        //将json数据保存到数据库中
        //获取当前用户的uid
        User user = (User) session.getAttribute("loginUser");
        int uid = user.getUid();
        user.setInterests(interestStr);
        int result = userService.updateUser("uid", uid, user);
        if (result > 0) {
            System.out.println("用户偏好更新成功");
            model.addAttribute("msg1", "用户偏好更新成功");
        } else {
            System.out.println("用户偏好更新失败");
            model.addAttribute("msg1", "用户偏好更新失败");
        }
        //重新设置session
        session.setAttribute("loginUser", user);

        //设置用户偏好model
        setInterestModel(user, session);

        return "account/user-profile";
    }

    /**
     * 提取公共代码，功能：设置用户偏好的model
     */
    public void setInterestModel(User user, HttpSession session) {
        JSONArray jsonArray = JSON.parseArray(user.getInterests());

        // model.addAttribute("interests", jsonArray);
        JSONArray carTypeArray = ((JSONObject) jsonArray.get(0)).getJSONArray("value");

        //这里不知道怎么传JSONArray到html页面，转换为Map进行传输
        Map<String, Boolean> carTypeMap = new LinkedHashMap<>();
        for (int i = 0; i < carTypeArray.size(); i++) {
            String key = (String) ((JSONObject) carTypeArray.get(i)).get("key");
            Boolean value = (Boolean) ((JSONObject) carTypeArray.get(i)).get("value");
            carTypeMap.put(key, value);
        }
        // model.addAttribute("CarType", carTypeMap);
        session.setAttribute("CarType", carTypeMap);

        JSONArray powerTypeArray = ((JSONObject) jsonArray.get(1)).getJSONArray("value");
        Map<String, Boolean> powerTypeMap = new LinkedHashMap<>();
        for (int i = 0; i < powerTypeArray.size(); i++) {
            String key = (String) ((JSONObject)powerTypeArray.get(i)).get("key");
            Boolean value = (Boolean) ((JSONObject) powerTypeArray.get(i)).get("value");
            powerTypeMap.put(key, value);
        }
        // model.addAttribute("PowerType", powerTypeMap);
        session.setAttribute("PowerType", powerTypeMap);

        JSONArray transTypeArray = ((JSONObject) jsonArray.get(2)).getJSONArray("value");
        Map<String, Boolean> transTypeMap = new LinkedHashMap<>();
        for (int i = 0; i < transTypeArray.size(); i++) {
            String key = (String) ((JSONObject)transTypeArray.get(i)).get("key");
            Boolean value = (Boolean) ((JSONObject) transTypeArray.get(i)).get("value");
            transTypeMap.put(key, value);
        }
        // model.addAttribute("TransType", transTypeMap);
        session.setAttribute("TransType", transTypeMap);
    }

}
