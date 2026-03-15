package com.wen.module.user.model.dto;

import lombok.Data;

/**
 * 用户信息响应 DTO
 */
@Data
public class UserInfoResponse {

    /**
     * 用户 ID
     */
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * Token
     */
    private String token;
}
