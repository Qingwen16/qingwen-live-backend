package com.wen.model.dto;

import com.wen.common.constant.LoginType;
import lombok.Data;

/**
 * 统一登录请求 DTO
 * 支持多种登录方式，通过 loginType 区分
 */
@Data
public class LoginRequest {

    /**
     * 登录类型
     * PHONE_CODE - 手机验证码
     * WECHAT - 微信（暂未实现）
     * PASSWORD - 密码（暂未实现）
     */
    private LoginType loginType;

    /**
     * 登录凭证
     * 手机号登录时：传手机号
     * 微信登录时：传 code
     * 密码登录时：传用户名/手机号
     */
    private String credential;

    /**
     * 验证信息
     * 手机号登录时：传验证码
     * 微信登录时：传 code
     * 密码登录时：传密码
     */
    private String verification;

    /**
     * 额外参数（JSON 格式）
     * 微信登录时可以传 encryptedData、iv 等
     */
    private String extraData;

}
