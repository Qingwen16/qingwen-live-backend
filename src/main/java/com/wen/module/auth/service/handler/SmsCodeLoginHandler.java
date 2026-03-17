package com.wen.module.auth.service.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wen.common.exception.BusinessException;
import com.wen.common.utils.UserContext;
import com.wen.module.auth.common.*;
import com.wen.module.auth.mapper.SmsCodeMapper;
import com.wen.module.auth.model.dto.LoginRequest;
import com.wen.module.auth.model.dto.SmsCodeCacheDto;
import com.wen.module.auth.model.entity.SmsCode;
import com.wen.module.auth.service.CacheService;
import com.wen.module.auth.service.LoginHandler;
import com.wen.module.user.mapper.UserInfoMapper;
import com.wen.module.user.model.dto.UserInfoResponse;
import com.wen.module.user.model.entity.UserInfo;
import com.wen.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 手机验证码登录处理器
 * 当前唯一实现的登录方式
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SmsCodeLoginHandler implements LoginHandler {

    private final CacheService cacheService;

    private final UserService userService;

    private final UserInfoMapper userInfoMapper;

    private final SmsCodeMapper smsCodeMapper;

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
    private void verifyCode(String phone, String code) {
        int type = SmsCodeTypeEnum.PHONE_LOGIN.getCode();
        SmsCodeCacheDto cacheDto = cacheService.getSmsCodeCacheDto(phone, type);
        if (cacheDto == null) {
            throw new BusinessException("验证码存在问题");
        }
        // 验证码一致
        if (code.equals(cacheDto.getCode())) {
            // 判断是否过期
            Long expireTime = cacheDto.getExpireTime();
            if (System.currentTimeMillis() > expireTime) {
                throw new BusinessException("验证码已过期");
            }
            // 判断是不是已使用
            if (cacheDto.getStatus() == SmsCodeStatusEnum.USED.getCode()) {
                throw new BusinessException("验证码已被使用");
            }
            long currentTime = System.currentTimeMillis();
            // 设置验证码被使用
            LambdaUpdateWrapper<SmsCode> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(SmsCode::getPhone, phone)
                    .eq(SmsCode::getCode, code)
                    .set(SmsCode::getUsedTime, currentTime)
                    .set(SmsCode::getUsedIp, "")
                    .set(SmsCode::getStatus, SmsCodeStatusEnum.USED.getCode());
            smsCodeMapper.update(updateWrapper);
            // 更新缓存
            cacheDto.setStatus(SmsCodeStatusEnum.USED.getCode());

        } else {
            throw new BusinessException("验证码错误");
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

        SmsCode smsCode = smsCodeMapper.selectOne(wrapper);
        if (smsCode != null) {
            smsCode.setStatus(1); // 已使用
            smsCode.setUsedTime(System.currentTimeMillis());
            smsCodeMapper.updateById(smsCode);
        }
    }

    /**
     * 构建登录响应
     */
    private UserInfoResponse buildLoginResponse(UserInfo userInfo) {
        // 更新登录时间
        userInfo.setUpdateTime(System.currentTimeMillis());
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
