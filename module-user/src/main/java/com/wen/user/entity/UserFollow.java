package com.wen.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户关注关系表
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@TableName("user_follow")
@Data
public class UserFollow {

    /**
     * 关注 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 被关注对象 ID
     */
    private Long followId;

    /**
     * 是否关注 0-否 1-是
     */
    private Integer isFollow;

    /**
     * 粉丝牌等级
     */
    private Integer fanMedalLevel;

    /**
     * 粉丝牌名称
     */
    private String fanMedalName;

    /**
     * 亲密度
     */
    private Long intimacy;

    /**
     * 是否特别关注 0-否 1-是
     */
    private Integer isSpecial;

    /**
     * 是否黑名单 0-否 1-是
     */
    private Integer isBlacklist;

    /**
     * 关注时间
     */
    private Long followTime;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;
}
