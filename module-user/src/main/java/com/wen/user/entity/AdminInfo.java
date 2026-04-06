package com.wen.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 管理员信息表
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@TableName("admin_info")
@Data
public class AdminInfo {

    /**
     * 管理员 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户 ID（关联 UserInfo）
     */
    private Long userId;

    /**
     * 管理员账号
     */
    private String adminAccount;

    /**
     * 管理员姓名
     */
    private String adminName;

    /**
     * 管理员类型 1-超级管理员 2-普通管理员 3-房管
     */
    private Integer adminType;

    /**
     * 权限标识
     */
    private String permissions;

    /**
     * 负责的房间 ID 列表（JSON 格式，房管用）
     */
    private String roomIds;

    /**
     * 管理员状态 0-禁用 1-正常
     */
    private Integer status;

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
}
