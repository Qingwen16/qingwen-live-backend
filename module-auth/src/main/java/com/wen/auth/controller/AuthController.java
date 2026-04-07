package com.wen.auth.controller;

import com.wen.auth.common.PhoneLoginRequest;
import com.wen.auth.common.WechatLoginRequest;
import com.wen.auth.service.AuthService;
import com.wen.auth.service.PhoneService;
import com.wen.auth.service.WechatService;
import com.wen.common.model.auth.TokenDto;
import com.wen.common.model.response.Response;
import com.wen.auth.common.SmsCodeRequest;
import com.wen.common.model.user.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author jwruan
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    private final WechatService weChatService;
    
    private final PhoneService phoneService;

    /**
     * 发送短信验证码
     */
    @PostMapping("/sms/send")
    public Response<Void> sendSmsCode(@RequestBody SmsCodeRequest request) {
        phoneService.sendSmsCode(request);
        return Response.success(null, "验证码已发送");
    }

    /**
     * 手机号验证码登录
     */
    @PostMapping("/phone/login")
    public Response<UserInfoResponse> phoneLogin(@RequestBody PhoneLoginRequest request) {
        UserInfoResponse response = phoneService.loginByPhone(request);
        return Response.success(response);
    }

    /**
     * 微信一键获取手机号登录
     */
    @PostMapping("/wechat/login")
    public Response<UserInfoResponse> weChatLogin(@RequestBody WechatLoginRequest request) {
        UserInfoResponse response = weChatService.loginByWeChat(request);
        return Response.success(response);
    }
        
    /**
     * 登出
     */
    @GetMapping("/logout")
    public Response<Void> logout(TokenDto request) {
        authService.logout(request);
        return Response.success(null, "登出成功");
    }

}
