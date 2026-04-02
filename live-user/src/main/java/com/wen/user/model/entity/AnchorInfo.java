package com.wen.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 主播信息表
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@TableName("anchor_info")
@Data
public class AnchorInfo {

    /**
     * 主播 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户 ID（关联 UserInfo）
     */
    private Long userId;

    /**
     * 主播昵称
     */
    private String anchorName;

    /**
     * 主播头像
     */
    private String avatar;

    /**
     * 主播简介
     */
    private String description;

    /**
     * 主播等级
     */
    private Integer level;

    /**
     * 粉丝数量
     */
    private Long fansCount;

    /**
     * 关注数量
     */
    private Long followCount;

    /**
     * 总观看次数
     */
    private Long totalViewCount;

    /**
     * 总收到礼物价值
     */
    private Long totalGiftValue;

    /**
     * 主播状态 0-禁用 1-正常 2-封禁
     */
    private Integer status;

    /**
     * 是否注销 0-注销 1-未注销
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;
}
