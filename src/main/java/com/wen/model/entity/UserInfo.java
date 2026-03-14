package com.wen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @Author : 青灯文案
 * @Date: 2026/3/13 23:52
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
     * 用户ID
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
     * 用户状态
     */
    private Integer status;

    /**
     * 用户是否注销  0 注销， 1 未注销
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
