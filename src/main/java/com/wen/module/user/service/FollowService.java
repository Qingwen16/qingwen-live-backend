package com.wen.module.user.service;

import com.wen.module.user.model.dto.FollowUserRequest;
import com.wen.module.user.model.entity.UserFollow;

import java.util.List;

/**
 * @Author : 青灯文案
 * @Date: 2026/3/16 19:56
 */
public interface FollowService {

    /**
     * 关注某用户
     */
    String followUser(FollowUserRequest request);

    /**
     * 取消关注某用户
     */
    String unfollowUser(FollowUserRequest request);

    /**
     * 获取某用户的关注列表
     */
    List<UserFollow> getUserFollowingList(Long userId);

    /**
     * 获取某用户的粉丝列表
     */
    List<UserFollow> getUserFollowersList(Long userId);
}
