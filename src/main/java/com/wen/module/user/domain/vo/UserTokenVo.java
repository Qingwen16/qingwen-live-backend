package com.wen.module.user.domain.vo;

import com.wen.module.auth.domain.vo.TokenInfo;
import lombok.Data;

/**
 * 用户信息响应 DTO
 */
@Data
public class UserTokenVo {

    /**
     * Token
     */
    private TokenInfo tokenInfo;

    /**
     * 用户 ID
     */
    private UserInfoVo userInfoVo;

}
