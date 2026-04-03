package com.wen.user.model.dto;

import com.wen.module.auth.model.dto.TokenDto;
import lombok.Data;

/**
 * 用户信息响应 DTO
 */
@Data
public class UserInfoResponse {

    /**
     * Token
     */
    private TokenDto tokenDto;

    /**
     * 用户 ID
     */
    private UserInfoDto userInfoDto;

}
