package com.wen.module.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.wen.common.exception.BusinessException;
import com.wen.config.CacheConfig;
import com.wen.module.auth.common.AuthConstants;
import com.wen.module.auth.mapper.SmsCodeMapper;
import com.wen.module.auth.model.dto.SmsCodeRequest;
import com.wen.module.auth.model.entity.SmsCode;
import com.wen.module.auth.service.CacheService;
import com.wen.module.auth.service.SmsCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 短信验证码服务
 *
 * @author jwruan
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsCodeServiceImpl implements SmsCodeService {

    private final CacheService cacheService;

    private final SmsCodeMapper smsCodeMapper;

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

        String code = generateCode();
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
     * 生成验证码
     */
    private String generateCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < AuthConstants.SMS_CODE_LENGTH; i++) {
            code.append((int) (Math.random() * 10));
        }
        return code.toString();
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

}
