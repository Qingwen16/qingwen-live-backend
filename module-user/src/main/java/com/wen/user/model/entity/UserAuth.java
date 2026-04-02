package com.wen.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wen.module.auth.common.AuthTypeEnum;
import lombok.Data;

/**
 * 用户认证信息实体类
 * 存储各种登录方式的认证信息（手机号、微信、QQ、微博等）
 */
@TableName("user_auth")
@Data
public class UserAuth {

    /**
     * 主键 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户 ID（关联 user_info.userId）
     */
    private Long userId;

    /**
     * 认证状态
     */
    private String phone;

    /**
     * 认证类型
     */
    private AuthTypeEnum authType;

    /**
     * 认证 ID（第三方平台的 OpenID、UnionID，或手机号、邮箱等）
     */
    private String authId;

    /**
     * 扩展信息（JSON 格式，存储 token、refresh_token 等）
     */
    private String extraData;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 最后登录时间
     */
    private Long lastLoginTime;
}
