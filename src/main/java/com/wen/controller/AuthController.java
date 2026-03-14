package com.wen.controller;

import com.wen.common.response.Response;
import com.wen.model.dto.LoginRequest;
import com.wen.model.dto.UserInfoResponse;
import com.wen.service.SMSCodeService;
import com.wen.service.auth.LoginFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
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

        String message = response.getIsNewUser() ?
                        "注册成功，欢迎加入青问直播！" : "登录成功！";

        return Response.success(response, message);
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
