package com.wen.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wen.common.exception.BusinessException;
import com.wen.mapper.UserInfoMapper;
import com.wen.model.dto.UserLoginRequest;
import com.wen.model.dto.UserInfoResponse;
import com.wen.model.dto.UserRegisterRequest;
import com.wen.model.entity.UserInfo;
import com.wen.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * 用户服务实现类
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserInfoMapper userInfoMapper;

    public UserServiceImpl(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    @Override
    public UserInfoResponse register(UserRegisterRequest request) {

        // 参数校验
        if (StrUtil.isBlank(request.getUsername())) {
            throw new BusinessException("用户名不能为空");
        }
        if (StrUtil.isBlank(request.getPassword())) {
            throw new BusinessException("密码不能为空");
        }

        // 允许用户名重复
        UserInfo existUser = userInfoMapper.selectByPhone(request.getPhone());

        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }

        // 检查手机号是否已存在
        if (StrUtil.isNotBlank(request.getPhone())) {
            LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserInfo::getPhone, request.getPhone())
                   .eq(UserInfo::getDeleted, 1);
            UserInfo existPhoneUser = userInfoMapper.selectOne(wrapper);
            if (existPhoneUser != null) {
                throw new BusinessException("手机号已被注册");
            }
        }

        // 创建用户
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(request, userInfo);

        // 密码加密
        userInfo.setPassword(BCrypt.hashpw(request.getPassword()));
        userInfo.setUserId(generateUserId());
        userInfo.setStatus(1);
        userInfo.setDeleted(1);
        long currentTime = Instant.now().toEpochMilli();
        userInfo.setCreateTime(currentTime);
        userInfo.setUpdateTime(currentTime);

        // 保存到数据库
        userInfoMapper.insert(userInfo);

        // 返回用户信息
        return buildUserInfoResponse(userInfo);
    }

    @Override
    public UserInfoResponse login(UserLoginRequest request) {
        // 参数校验
        if (StrUtil.isBlank(request.getAccount())) {
            throw new BusinessException("账号不能为空");
        }
        if (StrUtil.isBlank(request.getPassword())) {
            throw new BusinessException("密码不能为空");
        }

        // 查询用户（支持用户名或手机号登录）
        UserInfo userInfo = null;

        // 先尝试用户名
        userInfo = userInfoMapper.selectByUsername(request.getAccount());

        // 如果没找到，尝试手机号
        if (userInfo == null) {
            LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserInfo::getPhone, request.getAccount())
                   .eq(UserInfo::getDeleted, 1);
            userInfo = userInfoMapper.selectOne(wrapper);
        }

        if (userInfo == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证密码
        if (!BCrypt.checkpw(request.getPassword(), userInfo.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 检查用户状态
        if (userInfo.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        // 生成 Token（简单示例，实际应使用 JWT）
        String token = generateToken(userInfo.getId());

        // 构建响应
        UserInfoResponse response = buildUserInfoResponse(userInfo);
        response.setToken(token);

        return response;
    }

    @Override
    public UserInfoResponse getUserById(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if (userInfo == null || userInfo.getDeleted() == 0) {
            throw new BusinessException("用户不存在");
        }

        return buildUserInfoResponse(userInfo);
    }

    @Override
    public UserInfoResponse updateUserInfo(Long userId, UserRegisterRequest request) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if (userInfo == null || userInfo.getDeleted() == 0) {
            throw new BusinessException("用户不存在");
        }

        // 更新用户信息
        if (StrUtil.isNotBlank(request.getNickname())) {
            userInfo.setNickname(request.getNickname());
        }
        if (StrUtil.isNotBlank(request.getEmail())) {
            userInfo.setEmail(request.getEmail());
        }
        if (StrUtil.isNotBlank(request.getPhone())) {
            // 检查手机号是否被其他用户使用
            LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserInfo::getPhone, request.getPhone())
                   .ne(UserInfo::getId, userId)
                   .eq(UserInfo::getDeleted, 1);
            UserInfo existUser = userInfoMapper.selectOne(wrapper);
            if (existUser != null) {
                throw new BusinessException("手机号已被使用");
            }
            userInfo.setPhone(request.getPhone());
        }

        userInfo.setUpdateTime(Instant.now().toEpochMilli());
        userInfoMapper.updateById(userInfo);

        return buildUserInfoResponse(userInfo);
    }

    /**
     * 生成用户 ID
     */
    private Long generateUserId() {
        return Instant.now().toEpochMilli();
    }

    /**
     * 生成 Token（简化版本，生产环境建议使用 JWT）
     */
    private String generateToken(Long userId) {
        return "token_" + userId + "_" + System.currentTimeMillis();
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
