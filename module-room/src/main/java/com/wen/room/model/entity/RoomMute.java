package com.wen.room.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 房间禁言表
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@TableName("room_mute")
@Data
public class RoomMute {

    /**
     * 记录 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 直播间 ID
     */
    private Long roomId;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 操作管理员 ID
     */
    private Long operatorId;

    /**
     * 禁言原因
     */
    private String reason;

    /**
     * 禁言开始时间
     */
    private Long startTime;

    /**
     * 禁言结束时间
     */
    private Long endTime;

    /**
     * 禁言时长（秒）
     */
    private Long duration;

    /**
     * 禁言状态 0-已解除 1-禁言中
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
