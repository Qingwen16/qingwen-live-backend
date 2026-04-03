package com.wen.auth.service;

/**
 * @author : rjw
 * @date : 2026-03-17
 */
public interface CacheService {

    /**
     * 保存短信验证码发送记录(间隔控制)
     */
    void setSendSmsCodeInterval(String phone);

    /**
     * 查询手机号是否在发送间隔内，true-在间隔内(不能发送) false-不在间隔内(可以发送)
     */
    boolean getSendSmsCodeInterval(String phone);

    /**
     * 存储SmsCodeCacheDto
     */
    void setSmsCodeCache(String phone, String code);

    /**
     * 获取存储SmsCodeCacheDto
     */
    String getSmsCodeCache(String phone);

    /**
     * 获取存储SmsCodeCacheDto
     */
    void delSmsCodeCache(String phone);

    /**
     * 设置用户token
     */
    void setUserRefreshToken(Long userId, String refreshToken, Long timeout);

    /**
     * 获取用户token
     */
    String getUserRefreshToken(Long userId);

    /**
     * 删除用户token
     */
    void delUserRefreshToken(Long userId);

    /**
     * 将某一用户的AccessToken加入黑名单，用于即使下线登出
     */
    void setAccessTokenBlackList(String jti, Long timeout);

    /**
     * 将某一用户的AccessToken加入黑名单，用于即使下线登出
     */
    Boolean hasAccessTokenBlackList(String jti);


}
