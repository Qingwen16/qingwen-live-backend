package com.wen.module.auth.service;

import com.wen.module.auth.constant.AuthType;
import com.wen.module.auth.model.dto.LoginRequest;
import com.wen.module.user.model.dto.UserInfoResponse;

/**
 * 登录处理器接口
 * 每种登录方式都要实现这个接口
 */
public interface LoginHandler {

    /**
     * 处理登录请求
     * @param request 登录请求
     * @return 用户信息和 Token
     */
    UserInfoResponse login(LoginRequest request);

    /**
     * 获取支持的登录类型
     */
    AuthType getSupportedType();
}
