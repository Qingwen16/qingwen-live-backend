package com.wen.module.user.controller;

import com.wen.common.exception.BusinessException;
import com.wen.common.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : rjw
 * @date : 2026-03-16
 */
@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {

    /**
     * 关注某用户
     */
    @GetMapping("/followUser")
    public Response<String> followUser(@Param("userId") Long userId) {
        if (userId == null) {
            throw new BusinessException("输入参数不能为空");
        }
        return Response.success();
    }

    /**
     * 取消关注某用户
     */
    @GetMapping("/unfollowUser")
    public Response<String> unfollowUser(@Param("userId") Long userId) {
        if (userId == null) {
            throw new BusinessException("输入参数不能为空");
        }
        return Response.success();
    }

    /**
     * 获取某用户的关注列表
     */
    @GetMapping("/getUserFollowingList")
    public Response<String> getUserFollowingList(@Param("userId") Long userId) {
        if (userId == null) {
            throw new BusinessException("输入参数不能为空");
        }
        return Response.success();
    }

    /**
     * 获取某用户的关注列表
     */
    @GetMapping("/getUserFollowersList")
    public Response<String> getUserFollowersList(@Param("userId") Long userId) {
        if (userId == null) {
            throw new BusinessException("输入参数不能为空");
        }
        return Response.success();
    }

}
