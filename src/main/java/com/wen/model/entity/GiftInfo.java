package com.wen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 礼物信息表
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@TableName("gift_info")
@Data
public class GiftInfo {

    /**
     * 礼物 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 礼物名称
     */
    private String giftName;

    /**
     * 礼物图片
     */
    private String giftImage;

    /**
     * 礼物动态效果 URL
     */
    private String giftAnimationUrl;

    /**
     * 礼物价值（虚拟币单位）
     */
    private Long giftPrice;

    /**
     * 礼物类型 1-普通礼物 2-豪华礼物 3-特殊礼物
     */
    private Integer giftType;

    /**
     * 礼物等级
     */
    private Integer giftLevel;

    /**
     * 赠送时长（毫秒，用于持续礼物）
     */
    private Integer duration;

    /**
     * 礼物描述
     */
    private String description;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 是否热门 0-否 1-是
     */
    private Integer isHot;

    /**
     * 礼物状态 0-下架 1-上架
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
