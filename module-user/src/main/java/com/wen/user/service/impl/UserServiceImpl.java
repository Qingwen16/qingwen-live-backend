package com.wen.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wen.common.constant.RoleTypeEnum;
import com.wen.common.generator.UserIdGenerator;
import com.wen.common.model.user.UserInfoDto;
import com.wen.common.constant.DeleteEnum;
import com.wen.common.constant.StatusEnum;
import com.wen.common.model.user.UserRoleDto;
import com.wen.user.entity.UserInfo;
import com.wen.user.entity.UserRole;
import com.wen.user.mapper.UserInfoMapper;
import com.wen.user.mapper.UserRoleMapper;
import com.wen.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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

    private final UserRoleMapper userRoleMapper;

    /**
     * 创建通过第三方软件登录的用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoDto registerUser(String phone) {
        UserInfoDto userInfoDto = queryByPhone(phone);
        // 1. 存在信息直接返回
        if (userInfoDto != null) {
            log.info("手机号注册，该用户存在用户信息: {}", userInfoDto);
            return userInfoDto;
        }
        // 2. 新用户自动注册
        log.info("手机号用户注册：{}", phone);
        long currentTime = System.currentTimeMillis();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(UserIdGenerator.generator());
        userInfo.setUsername("phone_" + phone);
        userInfo.setPhone(phone);
        userInfo.setStatus(StatusEnum.NORMAL.getCode());
        userInfo.setDeleted(DeleteEnum.ACTIVE.getCode());
        userInfo.setCreateTime(currentTime);
        userInfo.setUpdateTime(currentTime);
        userInfoMapper.insert(userInfo);
        // 3. 设置用户角色
        UserRole userRole = new UserRole();
        userRole.setUserId(userInfo.getUserId());
        userRole.setUserName(userInfo.getUsername());
        userRole.setPhone(userInfo.getPhone());
        userRole.setRole(RoleTypeEnum.USER.getCode());
        userRole.setCreateTime(currentTime);
        userRole.setUpdateTime(currentTime);
        userRoleMapper.insert(userRole);
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

    @Override
    public List<UserInfo> queryByUserIdSet(Set<Long> userIdSet) {
        if (CollectionUtils.isEmpty(userIdSet)) {
            log.info("输入的查询用户ID数量为空");
            return Collections.emptyList();
        }
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UserInfo::getUserId, userIdSet);
        List<UserInfo> infoList = userInfoMapper.selectList(wrapper);
        log.info("查询到的用户信息数量: [{}]", infoList.size());
        return infoList;
    }
}