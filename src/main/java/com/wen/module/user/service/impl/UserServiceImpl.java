package com.wen.module.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wen.common.exception.BusinessException;
import com.wen.common.generator.SmsCodeGenerator;
import com.wen.module.auth.common.AuthConstants;
import com.wen.module.user.common.UserDeleteEnum;
import com.wen.common.generator.UserIdGenerator;
import com.wen.module.user.common.UserStatusEnum;
import com.wen.module.user.mapper.UserInfoMapper;
import com.wen.module.user.model.dto.UserInfoDto;
import com.wen.module.user.model.dto.UserInfoResponse;
import com.wen.module.user.model.dto.UserRegisterRequest;
import com.wen.module.user.model.dto.UserUpdateRequest;
import com.wen.module.user.model.entity.UserInfo;
import com.wen.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author : 青灯文案
 * @Date: 2026/3/14
 * 用户服务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserInfoMapper userInfoMapper;

    /**
     * 手机号注册
     */
    @Override
    public UserInfoDto registerByPassword(UserRegisterRequest request) {
        // 参数校验
        if (StrUtil.isBlank(request.getPhone())) {
            throw new BusinessException("用户手机号不能为空");
        }
        if (StrUtil.isBlank(request.getPassword())) {
            throw new BusinessException("密码不能为空");
        }
        // 允许用户名重复, 检查手机号是否已存在
        UserInfoDto userInfoDto = queryByPhone(request.getPhone());
        if (userInfoDto != null) {
            log.info("手机号注册，该用户存在用户信息: {}", userInfoDto);
            return userInfoDto;
        }
        // 2. 新用户自动注册
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(UserIdGenerator.generator());
        userInfo.setUsername("phone_" + request.getPhone());
        userInfo.setNickname("手机用户_" + SmsCodeGenerator.generateCode());
        userInfo.setPhone(request.getPhone());
        userInfo.setPassword(request.getPassword());
        userInfo.setStatus(UserStatusEnum.NORMAL.getCode());
        userInfo.setDeleted(UserDeleteEnum.ACTIVE.getCode());
        userInfo.setCreateTime(System.currentTimeMillis());
        userInfo.setUpdateTime(System.currentTimeMillis());
        userInfoMapper.insert(userInfo);
        log.info("手机号用户注册成功：createUser={}", userInfo);
        return buildUserInfoDto(userInfo);
    }

    /**
     * 创建通过第三方软件登录的用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoDto registerByPhone(String phone) {
        UserInfoDto userInfoDto = queryByPhone(phone);
        // 1. 存在信息直接返回
        if (userInfoDto != null) {
            log.info("手机号注册，该用户存在用户信息: {}", userInfoDto);
            return userInfoDto;
        }
        // 2. 新用户自动注册
        log.info("手机号用户注册：{}", phone);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(UserIdGenerator.generator());
        userInfo.setUsername("phone_" + phone);
        userInfo.setNickname("手机用户_" + SmsCodeGenerator.generateCode());
        userInfo.setPhone(phone);
        userInfo.setStatus(UserStatusEnum.NORMAL.getCode());
        userInfo.setDeleted(UserDeleteEnum.ACTIVE.getCode());
        userInfo.setCreateTime(System.currentTimeMillis());
        userInfo.setUpdateTime(System.currentTimeMillis());
        userInfoMapper.insert(userInfo);
        log.info("手机号用户注册成功：createUser={}", userInfo);
        return buildUserInfoDto(userInfo);
    }

    /**
     * 根据手机号查询用户信息
     */
    @Override
    public UserInfoDto queryByPhone(String phone) {
        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getPhone, phone));
        log.info("根据手机号 [{}] 查询用户成功 [{}]", phone, userInfo);
        return buildUserInfoDto(userInfo);
    }

    /**
     * 根据用户ID查询用户信息
     */
    @Override
    public UserInfoDto queryByUserId(Long userId) {
        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserId, userId));
        log.info("根据用户ID [{}] 查询用户成功 [{}]", userId, userInfo);
        return buildUserInfoDto(userInfo);
    }

    /**
     * 根据手机号和用户ID查询用户信息
     */
    @Override
    public UserInfo queryByPhoneAndUserId(String phone, Long userId) {
        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserId, userId)
                .eq(UserInfo::getPhone, phone));
        log.info("根据用户ID [{}] 和手机号 [{}] 查询用户成功 [{}]", userId, phone, userInfo);
        return userInfo;
    }

    /**
     * 构建用户信息返回对象
     */
    @Override
    public UserInfoResponse buildUserInfoResponse(UserInfoDto userInfoDto) {
        UserInfoResponse response = new UserInfoResponse();
        response.setUserInfoDto(userInfoDto);
        response.setToken("");
        return response;
    }

    /**
     * 更新用户信息
     */
    public String updateUserInfo(UserUpdateRequest request) {
        // 1. 参数校验
        verifyUserInfoParam(request);
        // 2. 根据 userId 和 phone 查询用户
        UserInfo userInfo = queryByPhoneAndUserId(request.getPhone(), request.getUserId());
        if (userInfo == null) {
            log.error("QueryByPhoneAndUserId: 数据库未查询到信息，新建数据");
            userInfo = new UserInfo();
            userInfo.setUserId(request.getUserId());
            userInfo.setPhone(request.getPhone());
            userInfo.setCreateTime(System.currentTimeMillis());
        }
        // 3. 更新用户信息（只更新非空字段）
        updateUserInfoFields(userInfo, request);
        userInfo.setUpdateTime(System.currentTimeMillis());
        userInfoMapper.updateById(userInfo);
        return "用户信息更新成功";
    }

    /**
     * 参数校验
     */
    private void verifyUserInfoParam(UserUpdateRequest request) {
        if (request.getUserId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        if (request.getPhone() == null || request.getPhone().isEmpty()) {
            throw new BusinessException("用户手机号不能为空");
        }
        if (!request.getPhone().matches(AuthConstants.PHONE_REGEX)) {
            throw new BusinessException("手机号格式不正确");
        }
    }

    /**
     * 更新用户信息字段
     */
    private void updateUserInfoFields(UserInfo userInfo, UserUpdateRequest request) {
        if (request.getUsername() != null) {
            userInfo.setUsername(request.getUsername());
        }
        if (request.getNickname() != null) {
            userInfo.setNickname(request.getNickname());
        }
        if (request.getEmail() != null) {
            userInfo.setEmail(request.getEmail());
        }
        if (request.getAvatar() != null) {
            userInfo.setAvatar(request.getAvatar());
        }
        if (request.getGender() != null) {
            userInfo.setGender(request.getGender());
        }
        if (request.getCountry() != null) {
            userInfo.setCountry(request.getCountry());
        }
        if (request.getProvince() != null) {
            userInfo.setProvince(request.getProvince());
        }
        if (request.getCity() != null) {
            userInfo.setCity(request.getCity());
        }
        if (request.getAddress() != null) {
            userInfo.setAddress(request.getAddress());
        }
        if (request.getZipCode() != null) {
            userInfo.setZipCode(request.getZipCode());
        }
    }

    /**
     * 构建用户信息响应
     */
    private UserInfoDto buildUserInfoDto(UserInfo userInfo) {
        if (userInfo == null) {
            return null;
        }
        UserInfoDto response = new UserInfoDto();
        BeanUtil.copyProperties(userInfo, response);
        return response;
    }
}