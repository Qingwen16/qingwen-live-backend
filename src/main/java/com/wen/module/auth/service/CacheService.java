package com.wen.module.auth.service;

import com.wen.module.auth.model.dto.SmsCodeCacheDto;

/**
 * @author : rjw
 * @date : 2026-03-17
 */
public interface CacheService {

    /**
     * 保存短信验证码发送记录(间隔控制)
     */
    void savePhoneSendInterval(String phone);

    /**
     * 查询手机号是否在发送间隔内
     * @param phone 手机号
     * @return true-在间隔内(不能发送) false-不在间隔内(可以发送)
     */
    boolean isInSendInterval(String phone);

    /**
     * 存储SmsCodeCacheDto
     */
    void saveSmsCodeCacheDto(SmsCodeCacheDto cacheDto);

    /**
     * 获取存储SmsCodeCacheDto
     * @param phone 手机号
     * @param type 验证码类型
     * @return 缓存信息
     */
    SmsCodeCacheDto getSmsCodeCacheDto(String phone, Integer type);

}
