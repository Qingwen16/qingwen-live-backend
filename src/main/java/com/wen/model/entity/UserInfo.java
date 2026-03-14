package com.wen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 用户信息实体类
 */
@TableName("user_info")
@Data
public class UserInfo {
    /**
     * ID
     */
    @TableId(type = IdType.INPUT)
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
     * 头像 URL
     */
    private String avatar;

    /**
     * 用户密码
     */
    @JsonIgnore
    @TableField(select = false)
    private String password;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户手机
     */
    private String phone;

    /**
     * 性别 0-未知 1-男 2-女
     */
    private Integer gender;

    /**
     * 微信 OpenID
     */
    private String wechatOpenid;

    /**
     * 微信 UnionID
     */
    private String wechatUnionId;

    /**
     * 用户状态 0-禁用 1-正常
     */
    private Integer status;

    /**
     * 用户是否注销 0-注销 1-未注销
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;
}
