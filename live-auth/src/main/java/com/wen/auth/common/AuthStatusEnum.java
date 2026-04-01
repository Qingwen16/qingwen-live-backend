package com.wen.auth.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 授权状态枚举
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@Getter
@AllArgsConstructor
public enum AuthStatusEnum {

    /**
     * 授权状态枚举
     */
    UNBOUND(0, "未绑定"),

    BOUND(1, "已绑定"),

    EXPIRED(2, "授权过期"),

    FAILED(3, "授权失败");

    private final Integer code;

    private final String desc;
}
