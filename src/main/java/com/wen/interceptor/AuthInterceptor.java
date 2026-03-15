package com.wen.interceptor;

import com.wen.module.user.mapper.UserInfoMapper;
import com.wen.module.user.model.entity.UserInfo;
import com.wen.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户认证拦截器
 * 解析 Token，设置用户上下文（游客或登录用户）
 */
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private RedisTemplate<String, Object> redisTemplate;

    private UserInfoMapper userInfoMapper;

    /**
     * Token 前缀
     */
    private static final String TOKEN_PREFIX = "user:token:";

    /**
     * Token 过期时间（30 天）
     */
    private static final long TOKEN_EXPIRE_DAYS = 30;

    @Override
    public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) throws Exception {

        // 从请求头获取 Token
        String token = request.getHeader("Authorization");

        if (token != null && !token.isEmpty()) {
            // 移除 Bearer 前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // 从 Redis 验证 Token
            String userIdStr = (String) redisTemplate.opsForValue().get(TOKEN_PREFIX + token);

            if (userIdStr != null) {
                // Token 有效，查询用户信息并设置到上下文
                try {
                    Long userId = Long.parseLong(userIdStr);
                    UserInfo userInfo = userInfoMapper.selectById(userId);

                    if (userInfo != null && userInfo.getStatus() == 1 && userInfo.getDeleted() == 0) {
                        // 用户状态正常，设置到上下文
                        UserContext.setUserInfo(userInfo);

                        // 刷新 Token 过期时间（滑动窗口）
                        redisTemplate.expire(TOKEN_PREFIX + token, TOKEN_EXPIRE_DAYS, java.util.concurrent.TimeUnit.DAYS);
                    }
                    // else: 用户被禁用或已注销，按游客处理
                } catch (NumberFormatException e) {
                    // Token 数据异常，按游客处理
                    UserContext.clear();
                }
            }
            // else: Token 无效或过期，保持游客状态
        }
        // else: 没有 Token，纯游客

        return true; // 继续执行后续处理
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                               HttpServletResponse response,
                               Object handler,
                               Exception ex) throws Exception {
        // 请求完成后清理上下文，防止内存泄漏
        UserContext.clear();
    }
}
