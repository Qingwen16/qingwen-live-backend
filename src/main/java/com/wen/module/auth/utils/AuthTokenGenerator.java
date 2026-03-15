package com.wen.module.auth.utils;

import org.springframework.stereotype.Component;

/**
 * Token 生成器
 * 简化版本，生产环境建议使用 JWT
 */
@Component
public class AuthTokenGenerator {

    /**
     * 生成 Token
     */
    public String generate(Long userId) {
        // 简单实现：userId + 时间戳 + 随机数
        return "token_" + userId + "_" + System.currentTimeMillis();
    }
}
