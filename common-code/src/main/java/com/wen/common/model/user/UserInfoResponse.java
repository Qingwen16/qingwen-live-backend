package com.wen.common.model.user;

import com.wen.common.model.auth.TokenDto;
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
