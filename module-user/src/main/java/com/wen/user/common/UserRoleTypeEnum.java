package com.wen.user.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : 青灯文案
 * @Date: 2026/3/16 20:29
 * 用户角色类型枚举 - 用于区分用户在平台中的身份和权限
 */
@AllArgsConstructor
@Getter
public enum UserRoleTypeEnum {

    /**
     * 游客 (未登录或临时访问用户，仅具有最基本的浏览权限)
     */
    GUEST(0, "游客"),

    /**
     * 普通用户 (基础用户角色，具有观看、关注、评论等基础权限)
     */
    NORMAL_USER(1, "普通用户"),

    /**
     * 主播 (已开通直播间，可以进行直播活动的主播用户)
     */
    STREAMER(2, "主播"),

    /**
     * 房管 (负责管理特定直播间的房间管理员，具有禁言、踢人等权限)
     */
    ROOM_ADMIN(3, "房管"),

    /**
     * 普通管理员 (平台级管理员，具有内容审核、用户管理等权限)
     */
    ADMIN(4, "管理员");

    private final int code;

    private final String desc;

    /**
     * 根据 code 获取枚举值
     */
    public static UserRoleTypeEnum valueOfCode(int code) {
        for (UserRoleTypeEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

    /**
     * 判断是否为管理员角色
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }

    /**
     * 判断是否为管理员角色
     */
    public boolean isRoomAdmin() {
        return this == ROOM_ADMIN;
    }

    /**
     * 判断是否为主播
     */
    public boolean isStreamer() {
        return this == STREAMER;
    }

    /**
     * 判断是否为普通用户
     */
    public boolean isNormalUser() {
        return this == NORMAL_USER;
    }

    /**
     * 判断是否为游客
     */
    public boolean isGuest() {
        return this == GUEST;
    }
}
