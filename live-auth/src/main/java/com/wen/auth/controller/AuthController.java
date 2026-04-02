package com.wen.auth.controller;

import com.wen.common.exception.BusinessException;
import com.wen.common.response.Response;
import com.wen.module.auth.common.AuthTypeEnum;
import com.wen.module.auth.common.LoginFactory;
import com.wen.module.auth.model.dto.LoginRequest;
import com.wen.module.auth.model.dto.SmsCodeRequest;
import com.wen.module.auth.model.dto.TokenDto;
import com.wen.module.auth.service.AuthService;
import com.wen.module.user.model.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 认证控制器
 *
 * @author jwruan
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginFactory loginFactory;

    private final AuthService authService;

    /**
     * 注册接口
     */
    @PostMapping("/register")
    public Response<UserInfoResponse> register(@RequestBody LoginRequest request) {
        if (request == null) {
            throw new BusinessException("输入参数不能为空");
        }
        UserInfoResponse response = loginFactory.executeLogin(request);
        return Response.success(response);
    }

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
    public Response<Void> sendSmsCode(@RequestBody SmsCodeRequest request) {
        if (request == null) {
            throw new BusinessException("输入参数不能为空");
        }
        authService.sendSmsCode(request);
        return Response.success(null, "验证码已发送");
    }

    /**
     * 获取支持的登录方式列表
     */
    @GetMapping("/login/types")
    public Response<Object> getLoginTypes() {
        List<AuthTypeEnum> result = loginFactory.getSupportedTypes();
        return Response.success(result);
    }

    /**
     * 刷新令牌
     */
    @PostMapping("/refreshUserToken")
    public Response<TokenDto> refreshUserToken(@RequestBody TokenDto request) {
        if (request == null) {
            throw new BusinessException("输入参数不能为空");
        }
        TokenDto tokenDto = authService.refreshUserToken(request);
        return Response.success(tokenDto);
    }

    /**
     * 统一登录接口
     */
    @PostMapping("/logout")
    public Response<?> logout(@RequestBody TokenDto request) {
        if (request == null) {
            throw new BusinessException("输入参数不能为空");
        }
        authService.logout(request);
        return Response.success();
    }


}
