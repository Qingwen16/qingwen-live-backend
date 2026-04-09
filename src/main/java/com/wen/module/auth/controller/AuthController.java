package com.wen.module.auth.controller;


import com.wen.common.response.Response;
import com.wen.module.auth.domain.vo.PhoneLoginRequest;
import com.wen.module.auth.domain.vo.SmsCodeRequest;
import com.wen.module.auth.domain.vo.TokenInfo;
import com.wen.module.auth.domain.vo.WechatLoginRequest;
import com.wen.module.auth.service.AuthService;
import com.wen.module.auth.service.PhoneService;
import com.wen.module.auth.service.WechatService;
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
    public Response<com.wen.module.user.domain.vo.UserTokenVo> phoneLogin(@RequestBody PhoneLoginRequest request) {
        com.wen.module.user.domain.vo.UserTokenVo response = phoneService.loginByPhone(request);
        return Response.success(response);
    }

    /**
     * 微信一键获取手机号登录
     */
    @PostMapping("/wechat/login")
    public Response<com.wen.module.user.domain.vo.UserTokenVo> weChatLogin(@RequestBody WechatLoginRequest request) {
        com.wen.module.user.domain.vo.UserTokenVo response = weChatService.loginByWeChat(request);
        return Response.success(response);
    }
        
    /**
     * 登出
     */
    @GetMapping("/logout")
    public Response<Void> logout(TokenInfo request) {
        authService.logout(request);
        return Response.success(null, "登出成功");
    }

}
