package com.wen.model.dto;

import lombok.Data;

/**
 * 用户注册请求 DTO
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@Data
public class UserRegisterRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;
}
