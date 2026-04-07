package com.wen.user.service;

import com.wen.common.model.user.UserInfoDto;
import com.wen.user.entity.UserInfo;

/**
 * 用户服务接口
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
public interface UserService {

    /**
     * 用户注册（手机等第三方注册）
     */
    UserInfoDto registerUser(String phone);

    /**
     * 根据 手机号 获取用户信息
     */
    UserInfoDto queryByPhone(String phone);

    /**
     * 根据 用户ID 获取用户信息
     */
    UserInfoDto queryByUserId(Long userId);

}
