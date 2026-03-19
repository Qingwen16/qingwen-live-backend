package com.wen.module.auth.service;

import com.wen.module.auth.model.dto.SmsCodeRequest;
import com.wen.module.auth.model.dto.TokenDto;

/**
 * @author : rjw
 * @date : 2026-03-19
 */
public interface AuthService {

    /**
     * 手机短信发送
     */
    void sendSmsCode(SmsCodeRequest request);

    /**
     * 刷新用户的token
     */
    TokenDto refreshUserToken(TokenDto tokenDto);

    /**
     * 用户登出
     */
    void logout(TokenDto tokenDto);

}
