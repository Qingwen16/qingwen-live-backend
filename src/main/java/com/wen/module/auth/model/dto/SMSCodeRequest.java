package com.wen.module.auth.model.dto;

import lombok.Data;

/**
 * @Author : 青灯文案
 * @Date: 2026/3/14 17:28
 */
@Data
public class SMSCodeRequest {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码类型 1-注册登录 2-绑定手机 3-找回密码
     */
    private Integer type = 1;
}
