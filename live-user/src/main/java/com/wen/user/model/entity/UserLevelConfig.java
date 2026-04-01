package com.wen.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户等级配置表
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@TableName("user_level_config")
@Data
public class UserLevelConfig {

    /**
     * 配置 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 等级名称
     */
    private String levelName;

    /**
     * 所需经验值
     */
    private Long requiredExperience;

    /**
     * 等级图标
     */
    private String levelIcon;

    /**
     * 等级颜色
     */
    private String levelColor;

    /**
     * 每日弹幕数量上限
     */
    private Integer dailyMessageLimit;

    /**
     * 每日赠送礼物价值上限
     */
    private Long dailyGiftLimit;

    /**
     * 弹幕颜色特权
     */
    private Integer colorPrivilege;

    /**
     * 进场特效特权
     */
    private Integer enterEffect;

    /**
     * 等级状态 0-禁用 1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;
}
