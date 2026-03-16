package com.wen.module.auth.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 第三方登录类型枚举
 *
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@Getter
@AllArgsConstructor
public enum AuthType {

    /**
     * 密码
     */
    PASSWORD(0, "密码", "password"),

    /**
     * 手机号
     */
    PHONE(1, "手机号", "phone"),

    /**
     * 邮箱登录
     */
    EMAIL(2, "邮箱", "email"),

    /**
     * 微信
     */
    WECHAT(3, "微信", "wechat"),

    /**
     * 支付宝
     */
    ALIPAY(4, "支付宝", "alipay"),

    /**
     * QQ
     */
    QQ(5, "QQ", "qq"),

    /**
     * 微博
     */
    WEIBO(6, "微博", "weibo"),

    /**
     * 抖音
     */
    DOUYIN(7, "抖音", "douyin"),

    /**
     * 快手
     */
    KUAISHOU(8, "快手", "kuaishou"),

    /**
     * Apple ID
     */
    APPLE(9, "Apple ID", "apple");

    /**
     * 类型代码
     */
    private final Integer code;

    /**
     * 中文名称
     */
    private final String name;

    /**
     * 英文标识
     */
    private final String key;

}
