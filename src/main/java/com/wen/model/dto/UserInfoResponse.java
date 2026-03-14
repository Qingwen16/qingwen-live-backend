package com.wen.model.dto;

import lombok.Data;

/**
 * 用户信息响应 DTO
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@Data
public class UserInfoResponse {

    /**
     * 用户 ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

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
