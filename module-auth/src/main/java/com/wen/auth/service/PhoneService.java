package com.wen.auth.service;

import com.wen.auth.common.PhoneLoginRequest;
import com.wen.auth.common.SmsCodeRequest;
import com.wen.common.model.user.UserInfoResponse;

/**
 * 登录处理器接口
 * 每种登录方式都要实现这个接口
 */
public interface PhoneService {

    /**
     * 手机短信发送
     */
    void sendSmsCode(SmsCodeRequest request);

    /**
     * 处理登录请求
     */
    UserInfoResponse loginByPhone(PhoneLoginRequest request);


}
