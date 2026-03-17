package com.wen.module.auth.service;


import com.wen.module.auth.model.dto.SmsCodeRequest;

/**
 * @Author : 青灯文案
 * @Date: 2026/3/14 18:15
 */
public interface SmsCodeService {

    /**
     * 手机短信发送
     */
    void sendSmsCode(SmsCodeRequest request);

}
