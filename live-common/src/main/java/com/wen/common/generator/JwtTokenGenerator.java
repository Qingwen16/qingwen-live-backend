package com.wen.common.generator;

import com.wen.module.auth.model.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author : rjw
 * @date : 2026-03-18
 * token双令牌设置，Access Token (访问令牌 / “通行证”)，Refresh Token (刷新令牌 / “续期凭证”)
 * ###### Access Token (访问令牌 / “通行证”)
 * - 它是客户端访问受保护资源的 唯一凭证 。在直播平台中，除了登录/注册等少数公开接口外，
 * - 所有需要认证的 API 请求（如获取用户信息、关注主播、发送弹幕）都必须在请求头中携带它。
 * - 服务器端的拦截器（ AuthInterceptor ）可以直接从令牌本身解析出用户信息（ userId , role ），无需再次查询数据库。。
 * - 为了安全 。Access Token 因为每次请求都要携带，所以它在网络中传输的频率最高，被截获的风险也最大。
 * - 设置很短的有效期，即使这个 Token 被黑客盗取了，他也只有最多 30 分钟的“作恶时间”。
 * - 30 分钟后，这个 Token 就会自动失效，黑客就无法再用它来冒充用户了。这是一种 减小风险敞口 的关键策略。
 * ###### Refresh Token (刷新令牌 / “续期凭证”)
 * - 它的 唯一作用 就是用来获取一个新的 Access Token。它本身 不能 用来访问任何业务 API。
 * - 当客户端发现 AccessToken 过期或即将过期时，它会向特定的、唯一的续期接口（例如 /api/auth/refresh ）发送这个 RefreshToken。
 * - 服务器验证这个 Refresh Token 的有效性后，会重新生成一套 全新 的 Access Token 和 Refresh Token 返回给客户端。
 * - 为了用户体验 。我们不希望用户每隔 30 分钟就重新输入一次手机号和验证码来登录。
 * - 通过长生命周期的 Refresh Token，App 可以在后台“静默地”为用户续期，用户完全感觉不到 Access Token 的过期和刷新过程，
 * - 实现了“一次登录，长期有效”的流畅体验。
 * ###### 是 Access Token 加入黑名单？
 * - 用户点击“登出”按钮 。客户端向后端发送登出请求，请求头里带着那个还有效的 Access Token。
 * - 后端会从 Redis 中删除与该用户关联的 Refresh Token。这样，即使用户的 Refresh Token 之前被盗了，
 * - 黑客也无法再用它来申请新的 Access Token 了。
 * - 用户刚刚登出时，他手里的那个 Access Token 可能还有 29 分钟才过期。黑客仍然可以用它继续访问 API 29 分钟！
 * - 为了解决这个问题，后端会从这个 Access Token 中解析出它的唯一ID ( jti )，
 * - 然后将这个 jti 放入 Redis 黑名单，并设置一个 29 分钟的过期时间。
 * - 黑客拿着偷来的 Access Token 再次请求业务 API。AuthInterceptor 拦截请求，解析出 jti 。
 * - 拦截器去 Redis 黑名单里一查，发现这个 jti 存在。立即拒绝请求！ 登出操作瞬间生效。
 * ###### 结论 ：
 * - 将 Access Token 的 jti 加入黑名单 ，是为了实现 立即、主动 的访问撤销。
 * - 从 Redis 中删除 Refresh Token ，是为了实现 长期、根本 的会话终结。
 */
@Component
@Slf4j
@Data
public class JwtTokenGenerator {

    @Value("${jwt.secret:mySecretKey123456789012345678901234567890}")
    private String secret;

    /**
     * 秒, 7200 (2小时)
     */
    @Value("${jwt.access-token-timeout: 7200}")
    private long accessTokenTimeout;

    /**
     * 秒, 604800 (7天)
     */
    @Value("${jwt.refresh-token-timeout: 604800}")
    private long refreshTokenTimeout;

    /**
     * 生成包含 Access Token 和 Refresh Token 的 Map
     */
    public TokenDto generateTokens(Long userId) {

        SecretKey key = createSecretKey();

        // --- 生成 Access Token ---，设置唯一的 JTI
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        // claims.put("role", role);

        String accessToken = Jwts.builder()
                .claims(claims)
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenTimeout))
                .signWith(key)
                .compact();

        // --- 生成 Refresh Token ---，只存放 userId，Refresh Token 也有自己的 JTI
        String refreshToken = Jwts.builder()
                .subject(String.valueOf(userId))
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenTimeout))
                .signWith(key)
                .compact();

        TokenDto tokenDto = new TokenDto();
        tokenDto.setAccessToken(accessToken);
        tokenDto.setRefreshToken(refreshToken);
        return tokenDto;
    }

    /**
     * 从 Token 中解析出 Claims
     */
    public Claims parseToken(String token) {
        SecretKey key = createSecretKey();
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 创建密钥
     */
    private SecretKey createSecretKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
    }

    /**
     * 检查 Token 是否过期
     */
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    /**
     * 从 Token 中获取 userId
     * @param token JWT token
     * @return 用户 ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 从 Refresh Token 中获取 userId（Refresh Token 使用 subject 存储 userId）
     * @param refreshToken 刷新令牌
     * @return 用户 ID
     */
    public Long getUserIdFromRefreshToken(String refreshToken) {
        Claims claims = parseToken(refreshToken);
        String subject = claims.getSubject();
        return subject != null ? Long.parseLong(subject) : null;
    }

}