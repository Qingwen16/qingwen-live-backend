package com.wen.module.user.model.dto;

import lombok.Data;

/**
 * @author : rjw
 * @date : 2026-03-16
 */
@Data
public class UpdatePasswordRequest {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户新密码
     */
    private String password;

}
