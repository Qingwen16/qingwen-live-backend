package com.wen.controller;

import com.wen.common.response.Response;
import com.wen.model.dto.UserLoginRequest;
import com.wen.model.dto.UserInfoResponse;
import com.wen.model.dto.UserRegisterRequest;
import com.wen.service.UserService;
import org.springframework.web.bind.annotation.*;

/**
 * 用户登录注册控制器
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册
     * @param request 注册请求
     * @return 用户信息
     */
    @PostMapping("/register")
    public Response<UserInfoResponse> register(@RequestBody UserRegisterRequest request) {
        UserInfoResponse response = userService.register(request);
        return Response.success(response, "注册成功");
    }

    /**
     * 用户登录
     * @param request 登录请求
     * @return 用户信息和 Token
     */
    @PostMapping("/login")
    public Response<UserInfoResponse> login(@RequestBody UserLoginRequest request) {
        UserInfoResponse response = userService.login(request);
        return Response.success(response, "登录成功");
    }

    // Deleted:@PostMapping("/login")
    // Deleted:public void userLogin(){
    // Deleted:}
}
