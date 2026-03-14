package com.wen.service.auth.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wen.common.constant.LoginType;
import com.wen.common.exception.BusinessException;
import com.wen.mapper.SMSCodeMapper;
import com.wen.mapper.UserInfoMapper;
import com.wen.model.dto.LoginRequest;
import com.wen.model.dto.UserInfoResponse;
import com.wen.model.entity.SMSCode;
import com.wen.model.entity.UserInfo;
import com.wen.service.auth.AuthTokenGenerator;
import com.wen.service.auth.LoginHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * 手机验证码登录处理器
 * 当前唯一实现的登录方式
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SMSCodeLoginHandler implements LoginHandler {

    private final UserInfoMapper userInfoMapper;
    private final SMSCodeMapper smsCodeMapper;
    private final AuthTokenGenerator tokenGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoResponse login(LoginRequest request) {
        String phone = request.getCredential();
        String code = request.getVerification();

        // 1. 参数校验
        validateParams(phone, code);

        // 2. 验证验证码
        verifyCode(phone, code);

        // 3. 查询或创建用户
        UserInfo userInfo = findOrCreateUser(phone);

        // 4. 标记验证码已使用
        markCodeAsUsed(phone, code);

        // 5. 构建响应
        return buildLoginResponse(userInfo);
    }

    @Override
    public LoginType getSupportedType() {
        return LoginType.PHONE;
    }

    /**
     * 参数校验
     */
    private void validateParams(String phone, String code) {
        if (StrUtil.isBlank(phone)) {
            throw new BusinessException("手机号不能为空");
        }

        if (!phone.matches("^1[3-9]\\d{9}$")) {
            throw new BusinessException("手机号格式不正确");
        }

        if (StrUtil.isBlank(code)) {
            throw new BusinessException("验证码不能为空");
        }
    }

    /**
     * 验证验证码
     */
    private void verifyCode(String phone, String code) {
        LambdaQueryWrapper<SMSCode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SMSCode::getPhone, phone)
               .eq(SMSCode::getCode, code)
               .eq(SMSCode::getStatus, 0) // 未使用
               .gt(SMSCode::getExpireTime, Instant.now().toEpochMilli()) // 未过期
               .orderByDesc(SMSCode::getCreateTime)
               .last("LIMIT 1");

        SMSCode verificationCode = smsCodeMapper.selectOne(wrapper);

        if (verificationCode == null) {
            throw new BusinessException("验证码错误或已过期");
        }
    }

    /**
     * 查找或创建用户
     */
    private UserInfo findOrCreateUser(String phone) {
        // 先查询是否存在
        UserInfo userInfo = userInfoMapper.selectByPhone(phone);

        if (userInfo != null && userInfo.getDeleted() == 1) {
            // 老用户直接返回
            log.info("手机号用户登录：{}", phone);
            return userInfo;
        }

        // 新用户自动注册
        log.info("手机号用户注册：{}", phone);
        return createNewUser(phone);
    }

    /**
     * 创建新用户
     */
    private UserInfo createNewUser(String phone) {
        long currentTime = Instant.now().toEpochMilli();

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(currentTime);
        userInfo.setUsername("phone_" + phone);
        userInfo.setNickname("手机用户_" + (phone.substring(7)));
        userInfo.setPhone(phone);
        userInfo.setStatus(1);
        userInfo.setDeleted(1);
        userInfo.setCreateTime(currentTime);
        userInfo.setUpdateTime(currentTime);

        userInfoMapper.insert(userInfo);

        return userInfo;
    }

    /**
     * 标记验证码已使用
     */
    private void markCodeAsUsed(String phone, String code) {
        LambdaQueryWrapper<SMSCode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SMSCode::getPhone, phone)
               .eq(SMSCode::getCode, code)
               .eq(SMSCode::getStatus, 0)
               .orderByDesc(SMSCode::getCreateTime)
               .last("LIMIT 1");

        SMSCode smsCode = smsCodeMapper.selectOne(wrapper);
        if (smsCode != null) {
            smsCode.setStatus(1); // 已使用
            smsCode.setUsedTime(Instant.now().toEpochMilli());
            smsCodeMapper.updateById(smsCode);
        }
    }

    /**
     * 构建登录响应
     */
    private UserInfoResponse buildLoginResponse(UserInfo userInfo) {
        // 更新登录时间
        userInfo.setUpdateTime(Instant.now().toEpochMilli());
        userInfoMapper.updateById(userInfo);

        UserInfoResponse response = new UserInfoResponse();
        response.setId(userInfo.getId());
        response.setUserId(userInfo.getUserId());
        response.setUsername(userInfo.getUsername());
        response.setNickname(userInfo.getNickname());
        response.setAvatar(userInfo.getAvatar());
        response.setGender(userInfo.getGender());
        response.setPhone(userInfo.getPhone());
        response.setEmail(userInfo.getEmail());
        response.setStatus(userInfo.getStatus());
        response.setToken(tokenGenerator.generate(userInfo.getId()));
        response.setIsNewUser(false); // 可以优化判断逻辑

        return response;
    }
}
