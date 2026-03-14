package com.wen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 手机号验证码实体类
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@TableName("SMS_code")
@Data
public class SMSCode {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;

    /**
     * 类型 1-注册登录 2-绑定手机 3-找回密码
     */
    private Integer type;

    /**
     * 状态 0-未使用 1-已使用 2-已过期
     */
    private Integer status;

    /**
     * 过期时间（毫秒时间戳）
     */
    private Long expireTime;

    /**
     * 使用时间
     */
    private Long usedTime;

    /**
     * 创建时间
     */
    private Long createTime;
}
