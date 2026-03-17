package com.wen.module.auth.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : rjw
 * @date : 2026-03-17
 */
@AllArgsConstructor
@Getter
public enum SmsCodeStatusEnum {

    /**
     * 手机验证码的类型
     */
    UNUSED(0, "未被使用"),

    USED(1, "已被使用"),

    EXPIRED(2, "已经过期");

    private final int code;

    private final String desc;

}
