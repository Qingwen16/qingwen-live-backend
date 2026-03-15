package com.wen.module.user.controller;

import com.wen.common.exception.BusinessException;
import com.wen.common.response.Response;
import com.wen.module.user.mapper.UserInfoMapper;
import com.wen.module.user.model.dto.UserUpdateRequest;
import com.wen.module.user.model.dto.UserInfoResponse;
import com.wen.module.user.model.dto.UserRegisterRequest;
import com.wen.module.user.model.entity.UserInfo;
import com.wen.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

/**
 * 用户信息控制器
 *
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@RestController
@RequestMapping("/userInfo")
@RequiredArgsConstructor
public class UserController {

    private UserService userService;

    /**
     * 获取用户信息
     */
    @GetMapping("/queryByPhone")
    public Response<UserInfoResponse> queryByPhone(@Param("phone") String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new BusinessException("输入参数不能为空");
        }
        UserInfoResponse response = userService.queryByPhone(phone);
        return Response.success(response);
    }

    @GetMapping("/queryByUserId")
    public Response<UserInfoResponse> queryByUserId(@Param("userId") Long userId) {
        if (userId == null) {
            throw new BusinessException("输入参数不能为空");
        }
        UserInfoResponse response = userService.queryByUserId(userId);
        return Response.success(response);
    }

    /**
     * 修改用户信息（根据用户ID和手机号查询信息，用户ID和手机号不允许更改，手机号可以重新注册或者解绑）
     */
    @PostMapping("/updateInfo")
    public Response<String> updateUserInfo(@RequestBody UserUpdateRequest request) {
        if (request == null) {
            throw new BusinessException("输入参数不能为空");
        }
        String result = userService.updateUserInfo(request);
        return Response.success(result);
    }

}
