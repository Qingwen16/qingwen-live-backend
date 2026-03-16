package com.wen.module.user.model.dto;

import lombok.Data;

/**
 * @Author : 青灯文案
 * @Date: 2026/3/16 20:17
 */
@Data
public class FollowUserRequest {

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 被关注对象 ID（主播 ID）
     */
    private Long followId;

}
