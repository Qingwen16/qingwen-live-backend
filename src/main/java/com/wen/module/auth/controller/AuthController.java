package com.wen.module.auth.controller;

import com.wen.common.response.Response;
import com.wen.module.auth.model.dto.LoginRequest;
import com.wen.module.user.model.dto.UserInfoResponse;
import com.wen.module.auth.service.SMSCodeService;
import com.wen.module.auth.utils.LoginFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginFactory loginFactory;
    private final SMSCodeService smsCodeService;

    /**
     * 统一登录接口
     * 支持多种登录方式，通过 loginType 区分
     */
    @PostMapping("/login")
    public Response<UserInfoResponse> login(@RequestBody LoginRequest request) {
        UserInfoResponse response = loginFactory.executeLogin(request);
        return Response.success(response);
    }

    /**
     * 发送短信验证码
     */
    @PostMapping("/sms/send")
    public Response<Void> sendSmsCode(
            @RequestParam String phone,
            @RequestParam(required = false, defaultValue = "1") Integer type) {
        smsCodeService.sendCode(phone, type);
        return Response.success(null, "验证码已发送");
    }

    /**
     * 获取支持的登录方式列表
     */
    @GetMapping("/login/types")
    public Response<Object> getLoginTypes() {
        return Response.success(loginFactory.getSupportedTypes());
    }
}
