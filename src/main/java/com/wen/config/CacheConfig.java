package com.wen.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @Author : 青灯文案
 * @Date: 2026/3/14 16:54
 */
@Data
@Configuration
public class CacheConfig {

    @Value("${cache.keyNameSpace}")
    private String keyNameSpace;

    /**
     * 默认时间类型
     */
    private TimeUnit defaultTimeUnit = TimeUnit.SECONDS;

    /**
     * 手机短信是否发送过的时间
     */
    private String keySendSmsCode = keyNameSpace + ":keySendSmsCode:";

    /**
     * 手机短信信息缓存
     */
    private String keySmsCodeCreateDto = keyNameSpace + ":keySmsCodeCreateDto";



}
