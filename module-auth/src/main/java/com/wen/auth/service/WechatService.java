package com.wen.auth.service;


import com.wen.auth.common.WechatLoginRequest;
import com.wen.common.model.user.UserInfoResponse;

/**
 * @author : rjw
 * @date : 2026-03-19
 */
public interface WechatService {

    /**
     * 处理登录请求
     */
    UserInfoResponse loginByWeChat(WechatLoginRequest request);

}
