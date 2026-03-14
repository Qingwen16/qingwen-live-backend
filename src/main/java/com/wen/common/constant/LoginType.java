package com.wen.common.constant;

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
public enum LoginType {

    /**
     * 微信
     */
    PHONE(0, "手机号", "phone"),

    /**
     * 微信
     */
    WECHAT(1, "微信", "wechat"),

    /**
     * QQ
     */
    QQ(2, "QQ", "qq"),

    /**
     * 微博
     */
    WEIBO(3, "微博", "weibo"),

    /**
     * 支付宝
     */
    ALIPAY(4, "支付宝", "alipay"),

    /**
     * 抖音
     */
    DOUYIN(5, "抖音", "douyin"),

    /**
     * 快手
     */
    KUAISHOU(6, "快手", "kuaishou"),

    /**
     * Apple ID
     */
    APPLE(7, "Apple ID", "apple"),

    /**
     * Google
     */
    GOOGLE(8, "Google", "google");

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
