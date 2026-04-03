package com.wen.auth.service;


import com.wen.common.enums.auth.LoginTypeEnum;
import com.wen.common.vo.auth.LoginRequest;
import com.wen.common.vo.user.UserInfoResponse;

/**
 * 登录处理器接口
 * 每种登录方式都要实现这个接口
 */
public interface LoginService {

    /**
     * 处理登录请求
     */
    UserInfoResponse login(LoginRequest request);

    /**
     * 获取支持的登录类型
     */
    LoginTypeEnum getSupportedType();
}
