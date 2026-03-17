package com.wen.module.user.service;

import com.wen.module.auth.common.AuthTypeEnum;
import com.wen.module.user.model.dto.UserInfoResponse;
import com.wen.module.user.model.dto.UserRegisterRequest;
import com.wen.module.user.model.dto.UserUpdateRequest;
import com.wen.module.user.model.entity.UserInfo;

/**
 * 用户服务接口
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
public interface UserService {

    /**
     * 用户注册（根据信息注册）
     */
    UserInfoResponse register(UserRegisterRequest request);

    /**
     * 用户注册（手机等第三方注册）
     */
    UserInfo registerByAuth(String phone, AuthTypeEnum authTypeEnum);

    /**
     * 根据 手机号 获取用户信息
     */
    UserInfoResponse queryByPhone(String phone);

    /**
     * 根据 用户ID 获取用户信息
     */
    UserInfoResponse queryByUserId(Long userId);

    /**
     * 更新用户信息
     */
    String updateUserInfo(UserUpdateRequest request);
}
