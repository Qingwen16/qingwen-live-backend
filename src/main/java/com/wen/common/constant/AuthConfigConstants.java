package com.wen.common.constant;

/**
 * 第三方登录配置常量
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
public class AuthConfigConstants {

    private AuthConfigConstants() {
        throw new IllegalStateException("Constant class");
    }

    // ==================== 微信配置 ====================

    /**
     * 微信开放平台 AppID 前缀
     */
    public static final String WECHAT_APP_ID_PREFIX = "wx";

    /**
     * 微信授权 URL 基础路径
     */
    public static final String WECHAT_AUTH_URL = "https://open.weixin.qq.com/connect/qrconnect";

    /**
     * 微信获取 AccessToken URL
     */
    public static final String WECHAT_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     * 微信获取用户信息 URL
     */
    public static final String WECHAT_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo";

    /**
     * 微信小程序登录凭证校验 URL
     */
    public static final String WECHAT_JS_CODE_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    // ==================== QQ 配置 ====================

    /**
     * QQ 互联授权 URL
     */
    public static final String QQ_AUTH_URL = "https://graph.qq.com/oauth2.0/authorize";

    /**
     * QQ 获取 AccessToken URL
     */
    public static final String QQ_ACCESS_TOKEN_URL = "https://graph.qq.com/oauth2.0/token";

    /**
     * QQ 获取 OpenID URL
     */
    public static final String QQ_OPENID_URL = "https://graph.qq.com/oauth2.0/me";

    /**
     * QQ 获取用户信息 URL
     */
    public static final String QQ_USER_INFO_URL = "https://graph.qq.com/user/get_user_info";

    // ==================== 微博配置 ====================

    /**
     * 微博授权 URL
     */
    public static final String WEIBO_AUTH_URL = "https://api.weibo.com/oauth2/authorize";

    /**
     * 微博获取 AccessToken URL
     */
    public static final String WEIBO_ACCESS_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";

    /**
     * 微博获取用户信息 URL
     */
    public static final String WEIBO_USER_INFO_URL = "https://api.weibo.com/2/users/show.json";

    // ==================== 支付宝配置 ====================

    /**
     * 支付宝授权 URL
     */
    public static final String ALIPAY_AUTH_URL = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm";

    /**
     * 支付宝获取 AccessToken URL
     */
    public static final String ALIPAY_ACCESS_TOKEN_URL = "https://openapi.alipay.com/gateway.do";

    /**
     * 支付宝获取用户信息 URL
     */
    public static final String ALIPAY_USER_INFO_URL = "https://openapi.alipay.com/gateway.do?method=alipay.user.info.share";

    // ==================== 抖音配置 ====================

    /**
     * 抖音授权 URL
     */
    public static final String DOUYIN_AUTH_URL = "https://open.douyin.com/platform/oauth/connect";

    /**
     * 抖音获取 AccessToken URL
     */
    public static final String DOUYIN_ACCESS_TOKEN_URL = "https://open.douyin.com/oauth/client_token/";

    /**
     * 抖音获取用户信息 URL
     */
    public static final String DOUYIN_USER_INFO_URL = "https://open.douyin.com/data/im/fetch";

    // ==================== 通用配置 ====================

    /**
     * 授权回调地址参数名
     */
    public static final String REDIRECT_URI_PARAM = "redirect_uri";

    /**
     * 应用 ID 参数名
     */
    public static final String APP_ID_PARAM = "appid";

    /**
     * 密钥参数名
     */
    public static final String SECRET_PARAM = "secret";

    /**
     * 授权范围
     */
    public static final String SCOPE_PARAM = "scope";

    /**
     * 默认授权范围（获取用户信息）
     */
    public static final String DEFAULT_SCOPE = "snsapi_userinfo";

    /**
     * 静默授权范围（仅获取 openid）
     */
    public static final String SILENT_SCOPE = "snsapi_base";

    /**
     * 授权响应类型
     */
    public static final String RESPONSE_TYPE = "code";

    /**
     * 状态参数（防止 CSRF）
     */
    public static final String STATE_PARAM = "state";

    /**
     * Token 有效期（2 小时，单位秒）
     */
    public static final long TOKEN_EXPIRE_SECONDS = 7200L;

    /**
     * Token 刷新提前时间（单位秒）
     */
    public static final long TOKEN_REFRESH_AHEAD_SECONDS = 300L;
}
