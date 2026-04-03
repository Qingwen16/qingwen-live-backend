package com.wen.auth.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : rjw
 * @date : 2026-03-17
 * 手机验证码的类型
 */
@AllArgsConstructor
@Getter
public enum SmsCodeTypeEnum {

    /**
     * 手机验证码的类型
     */
    PHONE_LOGIN(0, "手机登录"),

    BIND_PHONE(1, "绑定手机"),

    RESET_PASSWORD(2, "修改密码");

    private final int code;

    private final String desc;

}
