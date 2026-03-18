package com.wen.common.generator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : rjw
 * @date : 2026-03-18
 */
@Component
@Slf4j
public class TokenGenerator {

    @Value("${jwt.secret:mySecretKey123456789012345678901234567890}")
    private String secret;

    @Value("${jwt.expiration:604800000}")
    private Long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 Token
     */
    public String generateToken(Long userId, String phone) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("phone", phone);
        return createToken(claims, userId);
    }

    /**
     * 创建 Token
     */
    private String createToken(Map<String, Object> claims, Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)                       // 0.12.x 使用 claims() 替代 setClaims()
                .subject(String.valueOf(userId))      // 使用 subject() 替代 setSubject()
                .issuedAt(now)                        // 使用 issuedAt() 替代 setIssuedAt()
                .expiration(expiryDate)               // 使用 expiration() 替代 setExpiration()
                .signWith(getSigningKey())            // 简化签名方法
                .compact();
    }

    /**
     * 从 Token 中获取用户ID
     */
    public String getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", String.class);
    }

    /**
     * 从 Token 中获取手机号
     */
    public String getPhoneFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("phone", String.class);
    }

    /**
     * 解析 Token
     */
    private Claims parseToken(String token) {
        return Jwts.parser()                          // 0.12.x 使用 parser() 替代 parserBuilder()
                .verifyWith(getSigningKey())          // 使用 verifyWith() 替代 setSigningKey()
                .build()
                .parseSignedClaims(token)             // 使用 parseSignedClaims() 替代 parseClaimsJws()
                .getPayload();                        // 使用 getPayload() 替代 getBody()
    }

    /**
     * 验证 Token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token 验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 检查 Token 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 刷新 Token
     */
    public String refreshToken(String token) {
        Claims claims = parseToken(token);
        return createToken(new HashMap<>(claims), Long.valueOf(claims.getSubject()));
    }
}