package com.wen.common.dto.auth;

import lombok.Data;

/**
 * @author : rjw
 * @date : 2026-03-19
 * JwtTokenGenerator 生成的双令牌 token
 */
@Data
public class TokenDto {

    /**
     * 权限token 2小时
     */
    private String accessToken;

    /**
     * 刷新token 7天
     */
    private String refreshToken;

}
