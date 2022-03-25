package com.spider.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author wangrui
 * @Description TODO
 * @date 2022/3/15 14:49
 */
@TableName("users")
@Data
public class User {
    /*用户id*/
    private int uid;

    /*用户名，昵称*/
    private String username;

    /*用户登录密码*/
    private String password;

    /*用户头像*/
    private String userPhoto;

    /*用户地址*/
    private String location;

    /*用户手机号（用于登录系统）*/
    private String phone;

    /*用户邮箱*/
    private String email;

    /*用户类型*/
    private int state;

    /*用户偏好*/
    private String interests;
}
