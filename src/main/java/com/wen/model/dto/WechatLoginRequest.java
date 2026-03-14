package com.wen.model.dto;

import lombok.Data;

/**
 * 微信登录请求 DTO
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@Data
public class WechatLoginRequest {

    /**
     * 微信登录凭证 code（前端通过 wx.login 获取）
     */
    private String code;

    /**
     * 加密数据（用于获取手机号）
     */
    private String encryptedData;

    /**
     * 加密算法的初始向量
     */
    private String iv;
}
