package com.wen.model.dto;

import lombok.Data;

/**
 * 用户登录请求 DTO
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@Data
public class UserLoginRequest {

    /**
     * 用户名或手机号
     */
    private String account;

    /**
     * 密码
     */
    private String password;
}
