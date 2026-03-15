package com.wen.module.auth.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.wen.common.exception.BusinessException;
import com.wen.module.auth.mapper.SMSCodeMapper;
import com.wen.module.auth.model.entity.SMSCode;
import com.wen.module.auth.service.SMSCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * 短信验证码服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SMSCodeServiceImpl implements SMSCodeService {

    private final SMSCodeMapper smsCodeMapper;

    // 验证码有效期 5 分钟
    private static final long CODE_EXPIRE_SECONDS = 300L;

    /**
     * 发送验证码
     */
    @Override
    public void sendCode(String phone, Integer type) {
        // 1. 校验手机号
        if (StrUtil.isBlank(phone)) {
            throw new BusinessException("手机号不能为空");
        }

        if (!phone.matches("^1[3-9]\\d{9}$")) {
            throw new BusinessException("手机号格式不正确");
        }

        // 2. 生成 6 位验证码
        String code = RandomUtil.randomNumbers(6);
        long currentTime = Instant.now().toEpochMilli();
        long expireTime = currentTime + CODE_EXPIRE_SECONDS * 1000;

        // 3. 保存验证码
        SMSCode smsCode = new SMSCode();
        smsCode.setPhone(phone);
        smsCode.setCode(code);
        smsCode.setType(type);
        smsCode.setStatus(0); // 未使用
        smsCode.setExpireTime(expireTime);
        smsCode.setCreateTime(currentTime);

        smsCodeMapper.insert(smsCode);

        // 4. TODO: 调用短信服务商发送短信
        // 开发环境直接打印，生产环境接入阿里云/腾讯云
        log.info("【验证码】手机号={}, 验证码={}, 有效期 5 分钟", phone, code);
        System.out.println("【青问直播】您的验证码是：" + code + "，5 分钟内有效");
    }
}
