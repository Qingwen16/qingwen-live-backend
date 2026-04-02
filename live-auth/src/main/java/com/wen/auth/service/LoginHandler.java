package com.wen.auth.service;

import com.wen.module.auth.common.AuthTypeEnum;
import com.wen.module.auth.model.dto.LoginRequest;
import com.wen.module.user.model.dto.UserInfoResponse;

/**
 * 登录处理器接口
 * 每种登录方式都要实现这个接口
 */
public interface LoginHandler {

    /**
     * 处理登录请求
     */
    UserInfoResponse login(LoginRequest request);

    /**
     * 获取支持的登录类型
     */
    AuthTypeEnum getSupportedType();
}
