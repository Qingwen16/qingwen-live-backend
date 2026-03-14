package com.wen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wen.common.constant.AuthType;
import lombok.Data;

/**
 * 第三方登录绑定表
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@TableName("third_party_auth")
@Data
public class ThirdPartyAuth {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 第三方类型 1-微信 2-QQ 3-微博 4-支付宝 5-抖音 6-快手 7-Apple 8-Google
     */
    private Integer authType;

    /**
     * 第三方唯一标识（如 openid）
     */
    private String openId;

    /**
     * UnionID（微信等多应用统一标识）
     */
    private String unionId;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 令牌过期时间（毫秒时间戳）
     */
    private Long expireTime;

    /**
     * 授权状态 0-未绑定 1-已绑定 2-授权过期 3-授权失败
     */
    private Integer status;

    /**
     * 额外信息（JSON 格式，存储用户昵称、头像等）
     */
    private String extraInfo;

    /**
     * 最后登录时间
     */
    private Long lastLoginTime;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 获取授权类型枚举
     */
    public AuthType getAuthTypeEnum() {
        return AuthType.getByCode(this.authType);
    }

    /**
     * 检查是否已过期
     */
    public boolean isExpired() {
        if (this.expireTime == null) {
            return false;
        }
        return System.currentTimeMillis() >= this.expireTime;
    }

    /**
     * 检查是否需要刷新 Token
     * @param aheadSeconds 提前多少秒刷新
     */
    public boolean needRefreshToken(long aheadSeconds) {
        if (this.expireTime == null) {
            return true;
        }
        return System.currentTimeMillis() >= (this.expireTime - aheadSeconds * 1000);
    }
}
