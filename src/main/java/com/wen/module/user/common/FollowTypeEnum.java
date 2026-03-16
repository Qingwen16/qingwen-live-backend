package com.wen.module.user.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : 青灯文案
 * @Date: 2026/3/16 20:47
 */
@AllArgsConstructor
@Getter
public enum FollowTypeEnum {

    /**
     * 用户信息的状态
     */
    UNFOLLOW(0, "未关注"),

    FOLLOW(1, "关注");

    private final int code;

    private final String desc;

}
