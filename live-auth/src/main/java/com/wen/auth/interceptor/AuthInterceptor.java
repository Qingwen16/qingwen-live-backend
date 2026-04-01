package com.wen.auth.interceptor;

import com.wen.common.generator.JwtTokenGenerator;
import com.wen.common.utils.UserInfoContext;
import com.wen.module.auth.service.CacheService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户认证拦截器
 * 解析 Token，设置用户上下文（游客或登录用户）
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenGenerator jwtTokenGenerator;

    private final CacheService cacheService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. 从请求头获取 Token
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return false;
        }

        String accessToken = authHeader.substring(7);
        Claims claims;
        try {
            claims = jwtTokenGenerator.parseToken(accessToken);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
            return false;
        }

        // 2. 检查 Token 是否过期
        if (jwtTokenGenerator.isTokenExpired(claims)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
            return false;
        }

        String jti = claims.getId();
        Long userId = claims.get("userId", Long.class);
        String phone = claims.get("phone", String.class);

        // 3. 检查 Token 是否已被加入黑名单（主动失效）
        if (cacheService.hasAccessTokenBlackList(jti)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has been invalidated");
            return false;
        }

        // 4. 检查单点登录：这里我们通过校验 Refresh Token 是否存在来间接实现
        // 如果用户在别处登录，Redis 中的 Refresh Token 会被覆盖，导致旧的 Refresh Token 失效
        String storedRefreshToken = cacheService.getUserRefreshToken(userId);
        if (storedRefreshToken == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired, please login again");
            return false;
        }

        // 5. 所有检查通过，将用户信息存入 ThreadLocal，供后续业务使用
        // 例如: UserContextHolder.setUser(new AuthenticatedUser(userId, claims.get("role", String.class)));

        return true;
    }

    /**
     * 别忘了在 afterCompletion 中清理 ThreadLocal
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后清理上下文，防止内存泄漏
        UserInfoContext.clear();
    }
}
