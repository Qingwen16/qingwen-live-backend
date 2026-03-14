package com.wen.controller;

import com.wen.common.response.Response;
import com.wen.mapper.UserInfoMapper;
import com.wen.model.dto.AddressUpdateRequest;
import com.wen.model.dto.UserInfoResponse;
import com.wen.model.dto.UserRegisterRequest;
import com.wen.model.entity.UserInfo;
import com.wen.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户信息控制器
 *
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private UserService userService;

    private UserInfoMapper userInfoMapper;


    /**
     * 获取用户信息
     *
     * @param userId 用户 ID
     * @return 用户信息
     */
    @GetMapping("/{userId}")
    public Response<UserInfoResponse> getUserInfo(@PathVariable Long userId) {
        UserInfoResponse response = userService.getUserById(userId);
        return Response.success(response);
    }

    /**
     * 更新用户信息
     *
     * @param userId  用户 ID
     * @param request 更新请求
     * @return 更新后的用户信息
     */
    @PutMapping("/{userId}")
    public Response<UserInfoResponse> updateUserInfo(
            @PathVariable Long userId,
            @RequestBody UserRegisterRequest request) {
        UserInfoResponse response = userService.updateUserInfo(userId, request);
        return Response.success(response, "更新成功");
    }

    @PutMapping("/{userId}/address")
    public Response<Void> updateAddress(@PathVariable Long userId, @RequestBody AddressUpdateRequest request) {

        UserInfo userInfo = userInfoMapper.selectById(userId);
        if (userInfo == null) {
            return Response.fail(404, "用户不存在");
        }

        userInfo.setCountry(request.getCountry());
        userInfo.setProvince(request.getProvince());
        userInfo.setCity(request.getCity());
        userInfo.setAddress(request.getAddress());
        userInfo.setZipCode(request.getZipCode());
        userInfo.setUpdateTime(System.currentTimeMillis());

        userInfoMapper.updateById(userInfo);
        return Response.success(null, "地址更新成功");
    }

}
