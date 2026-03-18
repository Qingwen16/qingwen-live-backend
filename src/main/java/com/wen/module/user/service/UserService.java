package com.wen.module.user.service;

import com.wen.module.auth.common.AuthTypeEnum;
import com.wen.module.user.model.dto.UserInfoDto;
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
    UserInfoDto registerByPassword(UserRegisterRequest request);

    /**
     * 用户注册（手机等第三方注册）
     */
    UserInfoDto registerByPhone(String phone);

    /**
     * 根据 手机号 获取用户信息
     */
    UserInfoDto queryByPhone(String phone);

    /**
     * 根据 用户ID 获取用户信息
     */
    UserInfoDto queryByUserId(Long userId);

    /**
     * 根据 用户ID 获取用户信息
     */
    UserInfo queryByPhoneAndUserId(String phone, Long userId);

    /**
     * 将 UserInfoDto 转为 UserInfoResponse
     */
    UserInfoResponse buildUserInfoResponse(UserInfoDto userInfoDto);

    /**
     * 更新用户信息
     */
    String updateUserInfo(UserUpdateRequest request);
}
