package com.wen.module.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wen.common.utils.UserContext;
import com.wen.module.auth.common.AuthTypeEnum;
import com.wen.common.exception.BusinessException;
import com.wen.module.auth.common.TokenGenerator;
import com.wen.module.auth.mapper.SmsCodeMapper;
import com.wen.module.auth.model.entity.SmsCode;
import com.wen.module.user.mapper.UserInfoMapper;
import com.wen.module.auth.model.dto.LoginRequest;
import com.wen.module.user.model.dto.UserInfoResponse;
import com.wen.module.user.model.entity.UserInfo;
import com.wen.module.auth.service.LoginHandler;
import com.wen.module.user.service.UserService;
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
public class SmsCodeLoginHandler implements LoginHandler {

    private final UserService userService;

    private final UserInfoMapper userInfoMapper;

    private final SmsCodeMapper SmsCodeMapper;

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
        UserInfo userInfo = userService.registerByAuth(phone, AuthTypeEnum.PHONE);

        // 4. 登录成功，将用户信息存入本地
        UserContext.setUserInfo(userInfo);

        // 4. 标记验证码已使用
        markCodeAsUsed(phone, code);

        // 5. 构建响应
        return buildLoginResponse(userInfo);
    }

    @Override
    public AuthTypeEnum getSupportedType() {
        return AuthTypeEnum.PHONE;
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
        LambdaQueryWrapper<SmsCode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SmsCode::getPhone, phone)
               .eq(SmsCode::getCode, code)
               .eq(SmsCode::getStatus, 0) // 未使用
               .gt(SmsCode::getExpireTime, Instant.now().toEpochMilli()) // 未过期
               .orderByDesc(SmsCode::getCreateTime)
               .last("LIMIT 1");

        SmsCode verificationCode = SmsCodeMapper.selectOne(wrapper);

        if (verificationCode == null) {
            throw new BusinessException("验证码错误或已过期");
        }
    }

    /**
     * 标记验证码已使用
     */
    private void markCodeAsUsed(String phone, String code) {
        LambdaQueryWrapper<SmsCode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SmsCode::getPhone, phone)
               .eq(SmsCode::getCode, code)
               .eq(SmsCode::getStatus, 0)
               .orderByDesc(SmsCode::getCreateTime)
               .last("LIMIT 1");

        SmsCode SmsCode = SmsCodeMapper.selectOne(wrapper);
        if (SmsCode != null) {
            SmsCode.setStatus(1); // 已使用
            SmsCode.setUsedTime(Instant.now().toEpochMilli());
            SmsCodeMapper.updateById(SmsCode);
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
        response.setToken(TokenGenerator.generateToken(userInfo.getId()));
        return response;
    }
}
