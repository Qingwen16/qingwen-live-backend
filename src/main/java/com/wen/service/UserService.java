package com.wen.service;

import com.wen.model.dto.UserLoginRequest;
import com.wen.model.dto.UserInfoResponse;
import com.wen.model.dto.UserRegisterRequest;

/**
 * 用户服务接口
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
public interface UserService {

    /**
     * 用户注册
     */
    UserInfoResponse register(UserRegisterRequest request);

    /**
     * 用户登录
     */
    UserInfoResponse login(UserLoginRequest request);

    /**
     * 根据 ID 获取用户信息
     */
    UserInfoResponse getUserById(Long userId);

    /**
     * 更新用户信息
     */
    UserInfoResponse updateUserInfo(Long userId, UserRegisterRequest request);
}
