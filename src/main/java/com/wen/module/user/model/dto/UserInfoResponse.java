package com.wen.module.user.model.dto;

import lombok.Data;

/**
 * 用户信息响应 DTO
 */
@Data
public class UserInfoResponse {

    /**
     * Token
     */
    private String token;

    /**
     * 用户 ID
     */
    private UserInfoDto userInfoDto;

}
