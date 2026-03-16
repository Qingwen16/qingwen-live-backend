package com.wen.module.user.controller;

import com.wen.common.exception.BusinessException;
import com.wen.common.response.Response;
import com.wen.module.user.model.dto.FollowUserRequest;
import com.wen.module.user.model.entity.UserFollow;
import com.wen.module.user.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : rjw
 * @date : 2026-03-16
 */
@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    /**
     * 关注某用户
     */
    @PostMapping("/followUser")
    public Response<String> followUser(@RequestBody FollowUserRequest request) {
        if (request == null) {
            throw new BusinessException("输入参数不能为空");
        }
        String result = followService.followUser(request);
        return Response.success(result);
    }

    /**
     * 取消关注某用户
     */
    @GetMapping("/unfollowUser")
    public Response<String> unfollowUser(@RequestBody FollowUserRequest request) {
        if (request == null) {
            throw new BusinessException("输入参数不能为空");
        }
        String result = followService.unfollowUser(request);
        return Response.success(result);
    }

    /**
     * 获取某用户的关注列表
     */
    @GetMapping("/getUserFollowingList")
    public Response<List<UserFollow>> getUserFollowingList(@Param("userId") Long userId) {
        if (userId == null) {
            throw new BusinessException("输入参数不能为空");
        }
        List<UserFollow> result = followService.getUserFollowingList(userId);
        return Response.success(result);
    }

    /**
     * 获取某用户的关注列表
     */
    @GetMapping("/getUserFollowersList")
    public Response<List<UserFollow>> getUserFollowersList(@Param("userId") Long userId) {
        if (userId == null) {
            throw new BusinessException("输入参数不能为空");
        }
        List<UserFollow> result = followService.getUserFollowersList(userId);
        return Response.success(result);
    }

}
