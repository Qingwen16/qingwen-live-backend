package com.wen.module.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.wen.common.exception.BusinessException;
import com.wen.common.generator.JwtTokenGenerator;
import com.wen.common.generator.SmsCodeGenerator;
import com.wen.common.utils.UserInfoContext;
import com.wen.module.auth.common.AuthConstants;
import com.wen.module.auth.mapper.SmsCodeMapper;
import com.wen.module.auth.model.dto.SmsCodeRequest;
import com.wen.module.auth.model.dto.TokenDto;
import com.wen.module.auth.model.entity.SmsCode;
import com.wen.module.auth.service.AuthService;
import com.wen.module.auth.service.CacheService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : rjw
 * @date : 2026-03-19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CacheService cacheService;

    private final SmsCodeMapper smsCodeMapper;

    private final JwtTokenGenerator tokenGenerator;

    /**
     * 发送验证码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendSmsCode(SmsCodeRequest request) {
        // 1. 校验手机号
        if (StrUtil.isBlank(request.getPhone())) {
            throw new BusinessException("手机号不能为空");
        }
        if (!request.getPhone().matches(AuthConstants.PHONE_REGEX)) {
            throw new BusinessException("手机号格式不正确");
        }

        // 2. 不考虑发送次数检查，只检查发送间隔
        if (cacheService.getSendSmsCodeInterval(request.getPhone())) {
            throw new RuntimeException("发送过于频繁，请" + AuthConstants.SMS_CODE_SEND_INTERVAL_SECONDS + "秒后再试");
        }

        String code = SmsCodeGenerator.generateCode();
        long currentTime = System.currentTimeMillis();

        SmsCode smsCode = createSmsCode(request, code, currentTime);
        smsCodeMapper.insert(smsCode);

        // 存缓存验证码，存缓存这个手机发送验证码的时间间隔
        cacheService.setSmsCodeCache(request.getPhone(), code);
        cacheService.setSendSmsCodeInterval(request.getPhone());

        // 3. 调用短信服务商发送短信
        sendSmsCode(request.getPhone(), code);
        log.info("【验证码】手机号={}, 验证码={}, 有效期 5 分钟", request.getPhone(), code);
        System.out.println("【青问直播】您的验证码是：" + code + "，5 分钟内有效");
    }

    /**
     * 创建存储数据
     */
    private SmsCode createSmsCode(SmsCodeRequest request, String code, long currentTime) {
        SmsCode smsCode = new SmsCode();
        smsCode.setPhone(request.getPhone());
        smsCode.setCode(code);
        smsCode.setType(request.getType());
        smsCode.setSendIp(request.getIp());
        smsCode.setExpireTime(currentTime + AuthConstants.SMS_CODE_EXPIRE_MINUTES * 60 * 1000);
        smsCode.setUpdateTime(currentTime);
        smsCode.setCreateTime(currentTime);
        return smsCode;
    }

    /**
     * 发送短信
     */
    private void sendSmsCode(String phone, String code) {
        // 调用第三方短信服务
        log.info("发送短信：phone={}, code={}", phone, code);
    }

    @Override
    public TokenDto refreshUserToken(TokenDto tokenDto) {
        String requestRefreshToken = tokenDto.getRefreshToken();
        String requestAccessToken = tokenDto.getAccessToken();
        if (requestRefreshToken == null || requestAccessToken == null) {
            throw new BusinessException("输入参数的权限token或刷新token为空");
        }
        // 1. 解析客户端传来的 Refresh Token，获取 userId
        Claims claims;
        try {
            claims = tokenGenerator.parseToken(requestRefreshToken);
        } catch (Exception e) {
            throw new BusinessException("RefreshToken: Invalid Refresh Token");
        }
        // 2. 检查 Refresh Token 是否过期
        if (tokenGenerator.isTokenExpired(claims)) {
            throw new RuntimeException("RefreshToken: Refresh Token expired");
        }
        Long userId = Long.parseLong(claims.getSubject());
        String jti = claims.getId();

        // 3. 从 Redis 中获取该用户存储的 Refresh Token
        String cacheRefreshToken = cacheService.getUserRefreshToken(userId);

        // 4. 比对客户端传来的和 Redis 存储的是否一致
        if (cacheRefreshToken == null || !cacheRefreshToken.equals(requestRefreshToken)) {
            // 如果不一致，说明会话已失效（可能在别处登录或已登出）
            log.error("RefreshToken: request refresh token is null or not same");
            throw new RuntimeException("RefreshToken: Session has expired or is invalid. Please login again.");
        }

        // 5. 如果accessToken在黑名单中，也报错
        Boolean isHasToken = cacheService.hasAccessTokenBlackList(jti);
        if (isHasToken) {
            log.error("RefreshToken: AccessTokenBlackList has request access token");
            throw new RuntimeException("RefreshToken: Session has expired or is invalid. Please login again.");
        }

        // 5. 一致，说明验证通过，生成一套全新的 Token, 需要从数据库获取最新的用户角色信息，以防角色被管理员修改
        // String userRole = userService.getUserRoleById(userId);
        TokenDto result = tokenGenerator.generateTokens(userId);

        // 6. 将新的 Refresh Token 存入 Redis（覆盖旧的），保持单点登录
        cacheService.setUserRefreshToken(userId, result.getRefreshToken(), tokenGenerator.getRefreshTokenTimeout());
        return result;
    }

    @Override
    public void logout(TokenDto tokenDto) {
        // 1. 参数校验
        String requestRefreshToken = tokenDto.getRefreshToken();
        String requestAccessToken = tokenDto.getAccessToken();
        if (requestRefreshToken == null || requestAccessToken == null) {
            UserInfoContext.clear();
            return;
        }
        // 2. 解析 Access Token，获取 userId, jti 和 exp
        Claims claims;
        try {
            claims = tokenGenerator.parseToken(requestAccessToken);
        } catch (RuntimeException exception) {
            // 如果 Token 本身就无效，直接忽略即可，无需再做任何事
            UserInfoContext.clear();
            return;
        }

        Long userId = claims.get("userId", Long.class);
        String jti = claims.getId();

        // 3. 从 Redis 中删除用户的会话（即 Refresh Token），使其无法再续期
        cacheService.delUserRefreshToken(userId);
        cacheService.setAccessTokenBlackList(jti, tokenGenerator.getAccessTokenTimeout());
        UserInfoContext.clear();
    }
}
