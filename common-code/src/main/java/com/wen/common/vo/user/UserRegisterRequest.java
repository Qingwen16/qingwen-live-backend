package com.wen.common.vo.user;

import lombok.Data;

/**
 * 用户注册请求 DTO
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@Data
public class UserRegisterRequest {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

}
