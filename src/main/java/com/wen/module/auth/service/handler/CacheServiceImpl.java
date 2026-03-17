package com.wen.module.auth.service.impl;

import com.wen.config.CacheConfig;
import com.wen.module.auth.common.AuthConstants;
import com.wen.module.auth.model.dto.SmsCodeCacheDto;
import com.wen.module.auth.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 缓存服务实现类
 *
 * @author : rjw
 * @date : 2026-03-17
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final CacheConfig cacheConfig;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void savePhoneSendInterval(String phone) {
        String key = buildSendIntervalKey(phone);
        redisTemplate.opsForValue().set(key, "1", AuthConstants.SMS_CODE_SEND_INTERVAL_SECONDS,
                cacheConfig.getDefaultTimeUnit());
    }

    @Override
    public boolean isInSendInterval(String phone) {
        String key = buildSendIntervalKey(phone);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void saveSmsCodeCacheDto(SmsCodeCacheDto cacheDto) {
        String cacheKey = buildSmsCodeKey(cacheDto.getPhone(), cacheDto.getType());
        redisTemplate.opsForValue().set(cacheKey, cacheDto, AuthConstants.SMS_CODE_EXPIRE_MINUTES,
                cacheConfig.getDefaultTimeUnit());
        log.info("保存短信验证码缓存: phone={}, type={}", cacheDto.getPhone(), cacheDto.getType());
    }

    @Override
    public SmsCodeCacheDto getSmsCodeCacheDto(String phone, Integer type) {
        String cacheKey = buildSmsCodeKey(phone, type);
        return (SmsCodeCacheDto) redisTemplate.opsForValue().get(cacheKey);
    }

    /**
     * 构建发送间隔Key
     */
    private String buildSendIntervalKey(String phone) {
        return String.format(cacheConfig.getKeySendSmsCode(), phone);
    }

    /**
     * 构建短信验证码缓存Key
     */
    private String buildSmsCodeKey(String phone, Integer type) {
        return String.format("%s:%s:%s", cacheConfig.getKeySmsCodeCreateDto(), phone, type);
    }
}