package com.wen.service;

/**
 * @Author : 青灯文案
 * @Date: 2026/3/14 18:15
 */
public interface SMSCodeService {

    /**
     * 手机短信发送
     */
    void sendCode(String phone, Integer type);

}
