package com.wen.module.auth.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : rjw
 * @date : 2026-03-17
 */
@Data
public class SmsCodeCacheDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;

    /**
     * 类型：1-注册登录 2-绑定手机 3-找回密码
     */
    private Integer type;

    /**
     * 状态：0-未使用 1-已使用 2-已过期
     */
    private Integer status;

    /**
     * 过期时间（毫秒时间戳）
     */
    private Long expireTime;

    /**
     * 重试次数（验证失败次数）
     */
    private Integer retryCount;

    /**
     * 创建时间（毫秒时间戳）
     */
    private Long createTime;
}
