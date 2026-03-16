package com.wen.module.user.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : 青灯文案
 * @Date: 2026/3/16 20:00
 * 被关注的人的类型枚举
 */
@AllArgsConstructor
@Getter
public enum FollowUserTypeEnum {

    /**
     * 已注销的用户 (用户账号已注销)
     */
    DELETED(0, "已注销"),

    /**
     * 普通用户 (被关注的人是普通用户)
     */
    NORMAL_USER(1, "普通用户"),

    /**
     * 主播 (被关注的人是主播，具有直播间、开播状态等特殊属性)
     */
    STREAMER(2, "主播");

    private final int code;

    private final String desc;

}
