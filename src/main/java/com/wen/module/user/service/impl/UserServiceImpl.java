package com.wen.module.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wen.module.auth.common.AuthType;
import com.wen.common.exception.BusinessException;
import com.wen.module.user.common.DeleteStatus;
import com.wen.module.user.common.UserIdGenerator;
import com.wen.module.user.common.UserStatus;
import com.wen.module.user.mapper.UserInfoMapper;
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
 * 用户服务实现类
 *
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final long USER_ID_START = 1_000_000_000L;

    private final UserInfoMapper userInfoMapper;

    @Override
    public UserInfoResponse register(UserRegisterRequest request) {
        // 参数校验
        if (StrUtil.isBlank(request.getPhone())) {
            throw new BusinessException("用户手机号不能为空");
        }
        if (StrUtil.isBlank(request.getPassword())) {
            throw new BusinessException("密码不能为空");
        }

        // 允许用户名重复, 检查手机号是否已存在
        if (StrUtil.isNotBlank(request.getPhone())) {
            Long count = userInfoMapper.selectCount(new LambdaQueryWrapper<UserInfo>()
                    .eq(UserInfo::getPhone, request.getPhone())
                    .eq(UserInfo::getDeleted, DeleteStatus.ACTIVE.getCode())
            );

            if (count > 0) {
                throw new BusinessException("手机号已被注册");
            }
        }

        // 创建用户
        UserInfo createUser = new UserInfo();
        createUser.setUserId(UserIdGenerator.generator());
        // todo 密码未加密
        createUser.setPassword(request.getPassword());
        createUser.setStatus(UserStatus.NORMAL.getCode());
        createUser.setDeleted(DeleteStatus.ACTIVE.getCode());
        createUser.setCreateTime(System.currentTimeMillis());
        createUser.setUpdateTime(System.currentTimeMillis());
        userInfoMapper.insert(createUser);
        // 返回用户信息
        return buildUserInfoResponse(createUser);
    }

    /**
     * 创建通过第三方软件登录的用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfo registerByAuth(String phone, AuthType authType) {
        // 1. 先查询是否存在（加锁或使用唯一索引）
        List<UserInfo> userInfoList = userInfoMapper.selectList(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getPhone, phone)
        );

        if (!userInfoList.isEmpty()) {
            UserInfo userInfo = userInfoList.get(0);
            // 检查用户状态
            if (userInfo.getStatus() == UserStatus.DISABLED.getCode()) {
                throw new BusinessException("账号已被禁用");
            }
            if (userInfo.getDeleted() == DeleteStatus.DELETED.getCode()) {
                throw new BusinessException("账号已被删除");
            }
            log.info("手机号用户登录，用户信息：{}", userInfo);
            return userInfo;
        }

        // 2. 新用户自动注册
        log.info("手机号用户注册：{}", phone);
        UserInfo createUser = new UserInfo();
        createUser.setUserId(UserIdGenerator.generator());
        createUser.setUsername("phone_" + phone);
        createUser.setNickname("手机用户_" + (phone.length() > 7 ? phone.substring(7) : phone));
        createUser.setPhone(phone);
        createUser.setStatus(UserStatus.NORMAL.getCode());
        createUser.setDeleted(DeleteStatus.ACTIVE.getCode());
        createUser.setCreateTime(System.currentTimeMillis());
        createUser.setUpdateTime(System.currentTimeMillis());
        userInfoMapper.insert(createUser);
        log.info("手机号用户注册成功：createUser={}", createUser);
        return createUser;
    }

    @Override
    public UserInfoResponse queryByPhone(String phone) {
        List<UserInfo> userInfoList = userInfoMapper.selectList(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getPhone, phone));
        if (userInfoList.isEmpty()) {
            throw new BusinessException("用户不存在");
        }
        UserInfo userInfo = userInfoList.get(0);
        return buildUserInfoResponse(userInfo);
    }

    @Override
    public UserInfoResponse queryByUserId(Long userId) {
        List<UserInfo> userInfoList = userInfoMapper.selectList(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserId, userId));
        if (userInfoList.isEmpty()) {
            throw new BusinessException("用户不存在");
        }
        UserInfo userInfo = userInfoList.get(0);
        return buildUserInfoResponse(userInfo);
    }

    /**
     * 更新用户信息
     */
    public String updateUserInfo(UserUpdateRequest request) {
        Long userId = request.getUserId();
        String phone = request.getPhone();

        // 1. 参数校验
        if (userId == null) {
            throw new BusinessException("用户 ID 不能为空");
        }
        if (!isValidPhone(phone)) {
            throw new BusinessException("手机号格式不正确");
        }

        // 2. 根据 userId 和 phone 查询用户
        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserId, userId)
                .eq(UserInfo::getPhone, phone)
        );

        if (userInfo == null) {
            throw new BusinessException("用户不存在");
        }

        // 3. 更新用户信息（只更新非空字段）
        updateUserInfoFields(userInfo, request);

        // 4. 保存到数据库
        userInfo.setUpdateTime(System.currentTimeMillis());
        userInfoMapper.updateById(userInfo);

        return "用户信息更新成功";
    }


    /**
     * 验证手机号格式
     */
    private boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return phone.matches("^1[3-9]\\d{9}$");
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
    private UserInfoResponse buildUserInfoResponse(UserInfo userInfo) {
        UserInfoResponse response = new UserInfoResponse();
        response.setId(userInfo.getId());
        response.setUsername(userInfo.getUsername());
        response.setNickname(userInfo.getNickname());
        response.setPhone(userInfo.getPhone());
        response.setEmail(userInfo.getEmail());
        response.setStatus(userInfo.getStatus());
        return response;
    }
}
