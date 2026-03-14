
package com.wen.model.dto;

import lombok.Data;

/**
 * 微信用户信息 DTO
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@Data
public class WechatUserInfo {

    /**
     * 微信开放平台唯一标识（openId）
     */
    private String openId;

    /**
     * 微信unionId（多个应用同一用户）
     */
    private String unionId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 性别 1-男 2-女 0-未知
     */
    private Integer sex;

    /**
     * 用户头像
     */
    private String headImgUrl;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 语言
     */
    private String language;

    /**
     * 手机号（需要额外获取）
     */
    private String phone;
}