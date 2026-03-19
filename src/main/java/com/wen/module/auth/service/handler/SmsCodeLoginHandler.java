package com.wen.module.auth.service.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wen.common.exception.BusinessException;
import com.wen.common.generator.JwtTokenGenerator;
import com.wen.common.utils.UserInfoContext;
import com.wen.module.auth.common.*;
import com.wen.module.auth.mapper.SmsCodeMapper;
import com.wen.module.auth.model.dto.LoginRequest;
import com.wen.module.auth.model.dto.TokenDto;
import com.wen.module.auth.model.entity.SmsCode;
import com.wen.module.auth.service.CacheService;
import com.wen.module.auth.service.LoginHandler;
import com.wen.module.user.common.UserDeleteEnum;
import com.wen.module.user.common.UserStatusEnum;
import com.wen.module.user.model.dto.UserInfoDto;
import com.wen.module.user.model.dto.UserInfoResponse;
import com.wen.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 手机验证码登录处理器
 * 当前唯一实现的登录方式
 *
 * @author jwruan
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SmsCodeLoginHandler implements LoginHandler {

    private final JwtTokenGenerator jwtTokenGenerator;

    private final CacheService cacheService;

    private final UserService userService;

    private final SmsCodeMapper smsCodeMapper;

    /**
     * 用户获取到了验证码进行登录（如果存在该手机的用户直接登录，没有就直接注册）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoResponse login(LoginRequest request) {
        String phone = request.getCredential();
        String code = request.getVerification();
        String ip = request.getIp();
        // 1. 参数校验
        validateParams(phone, code);
        // 2. 验证验证码
        verifyCode(phone, code, ip);
        // 3. 查询用户
        UserInfoDto user = buildUserInfoDto(phone);
        // 4. 登录成功，将用户信息存入本地
        UserInfoContext.setUserInfo(user);
        // 5. 生成 Token
        TokenDto tokenDto = jwtTokenGenerator.generateTokens(user.getUserId());
        // 6. 缓存 Token
        cacheService.setUserRefreshToken(user.getUserId(), tokenDto.getRefreshToken(),
                jwtTokenGenerator.getRefreshTokenTimeout());
        // 5. 构建响应
        UserInfoResponse response = new UserInfoResponse();
        response.setUserInfoDto(user);
        response.setTokenDto(tokenDto);
        return response;
    }

    /**
     * 提供该实现类的实现类型
     */
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
        if (!phone.matches(AuthConstants.PHONE_REGEX)) {
            throw new BusinessException("手机号格式不正确");
        }
        if (StrUtil.isBlank(code)) {
            throw new BusinessException("验证码不能为空");
        }
    }

    /**
     * 验证验证码
     */
    private void verifyCode(String phone, String code, String ip) {
        // 缓存获取验证码
        String cacheCode = cacheService.getSmsCodeCache(phone);
        if (cacheCode == null) {
            throw new BusinessException("验证码存在问题，请重新发送验证码");
        }
        if (!cacheCode.equals(code)) {
            throw new BusinessException("验证码存在问题，请重新发送验证码");
        }
        long currentTime = System.currentTimeMillis();
        // 设置验证码被使用
        LambdaUpdateWrapper<SmsCode> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SmsCode::getPhone, phone)
                .eq(SmsCode::getCode, code)
                .set(SmsCode::getUsedTime, currentTime)
                .set(SmsCode::getUpdateTime, currentTime)
                .set(SmsCode::getUsedIp, ip);
        smsCodeMapper.update(updateWrapper);
        // 更新缓存，清楚缓存中的code，防止重复使用
        cacheService.delSmsCodeCache(phone);
    }

    /**
     * 查询用户信息
     */
    private UserInfoDto buildUserInfoDto(String phone) {
        UserInfoDto userInfoDto = userService.queryByPhone(phone);
        // 用户存在，则直接组装返回
        if (userInfoDto != null) {
            if (userInfoDto.getStatus() == UserStatusEnum.DISABLED.getCode()) {
                throw new BusinessException("该账号 [" + phone + "] 已被禁用");
            }
            if (userInfoDto.getDeleted() == UserDeleteEnum.DELETED.getCode()) {
                throw new BusinessException("该账号 [" + phone + "] 已被注销");
            }
            return userInfoDto;
        }
        // 用户不存在，注册用户
        return userService.registerByPhone(phone);
    }

}
