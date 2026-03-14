package com.wen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 直播分区表
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@TableName("live_category")
@Data
public class LiveCategory {

    /**
     * 分区 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分区名称
     */
    private String categoryName;

    /**
     * 分区图标
     */
    private String categoryIcon;

    /**
     * 分区描述
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
     * 分区状态 0-隐藏 1-显示
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
