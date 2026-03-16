package com.wen.module.auth.common;

import java.util.UUID;

/**
 * Token 生成工具类
 * 用于生成用户登录凭证
 */
public class TokenGenerator {

    /**
     * Token 前缀（用于识别 Token 类型）
     */
    private static final String TOKEN_PREFIX = "token_";

    /**
     * 生成用户登录 Token
     *
     * @param userId 用户 ID
     * @return Token 字符串
     */
    public static String generateToken(Long userId) {
        // 使用 UUID 保证唯一性和不可预测性
        String uuid = UUID.randomUUID().toString().replace("-", "");

        // 组合：前缀 + userId + UUID + 时间戳
        long timestamp = System.currentTimeMillis();
        return String.format("%s%d_%s_%d", TOKEN_PREFIX, userId, uuid, timestamp);
    }

    /**
     * 从 Token 中提取用户 ID
     *
     * @param token Token 字符串
     * @return 用户 ID，如果格式错误返回 null
     */
    public static Long getUserIdFromToken(String token) {
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return null;
        }

        try {
            // 去掉前缀
            String content = token.substring(TOKEN_PREFIX.length());
            // 按分隔符拆分
            String[] parts = content.split("_");
            if (parts.length >= 1) {
                return Long.parseLong(parts[0]);
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    /**
     * 验证 Token 格式是否合法
     *
     * @param token Token 字符串
     * @return true-格式合法，false-格式错误
     */
    public static boolean isValidFormat(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        if (!token.startsWith(TOKEN_PREFIX)) {
            return false;
        }

        try {
            String content = token.substring(TOKEN_PREFIX.length());
            String[] parts = content.split("_");

            // 至少应该有 3 部分：userId + uuid + timestamp
            if (parts.length < 3) {
                return false;
            }

            // 验证 userId 是数字
            Long.parseLong(parts[0]);

            // 验证 timestamp 是数字
            Long.parseLong(parts[parts.length - 1]);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取 Token 的生成时间
     *
     * @param token Token 字符串
     * @return 时间戳，失败返回 null
     */
    public static Long getTimestampFromToken(String token) {
        if (!isValidFormat(token)) {
            return null;
        }

        try {
            String content = token.substring(TOKEN_PREFIX.length());
            String[] parts = content.split("_");
            // 最后一部分是时间戳
            return Long.parseLong(parts[parts.length - 1]);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检查 Token 是否过期
     *
     * @param token Token 字符串
     * @param expireMillis 过期时间（毫秒）
     * @return true-未过期，false-已过期
     */
    public static boolean isExpired(String token, long expireMillis) {
        Long tokenTime = getTimestampFromToken(token);
        if (tokenTime == null) {
            return true; // 格式错误视为过期
        }

        long currentTime = System.currentTimeMillis();
        return (currentTime - tokenTime) > expireMillis;
    }
}
