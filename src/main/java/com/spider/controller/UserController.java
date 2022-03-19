package com.spider.controller;

import com.spider.pojo.User;
import com.spider.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/17 17:22
 */
@Controller
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 更新个人信息，可修改的信息包括：用户名（可被用来登录），手机号码，邮件，所在地，头像（单独更新）
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

        if (result > 0){
            model.addAttribute("msg","更新成功");
            System.out.println("更新成功");
            session.setAttribute("loginUser", user);
        } else {
            model.addAttribute("msg", "更新失败");
            System.out.println("更新失败");
        }
        return "user-profile";
    }

    /**
     * 重置密码
     * @return
     */
    @RequestMapping("/user/reset")
    public String reset(@RequestParam String oldPassword,
                        @RequestParam String newPassword,
                        Model model, HttpSession session){

        User user = (User) session.getAttribute("loginUser");

        //先查询原密码是否正确
        User selectUser = userService.queryUserById("uid", user.getUid());
        if (!(selectUser.getPassword().equals(oldPassword))){
            //如果原密码输入错误则更新失败
            model.addAttribute("msg", "原密码错误");
        } else {
            user.setPassword(newPassword);

            int result = userService.updateUser("uid", user.getUid(), user);

            if (result>0){
                //清除session
                session.invalidate();
                model.addAttribute("msg", "密码更新成功，请重新登录");
            } else {
                model.addAttribute("msg", "密码更新失败");
            }
        }
        return "account/reset-password";

    }

    @RequestMapping("/changeImg")
    public String uploadImg(@RequestParam String srcString, HttpSession session){
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

        return "redirect:/userProfile";
    }

    /**
     * 重置密码之后需要清除session再返回登录页面
     * @param session
     * @return
     */
    @RequestMapping("/BackToLogin")
    public String backToLogin(HttpSession session){
        //清除session
        session.invalidate();
        return "redirect:/login.html";
    }

}
