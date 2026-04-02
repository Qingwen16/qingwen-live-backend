package com.wen.room.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 弹幕消息表
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@TableName("room_message")
@Data
public class RoomMessage {

    /**
     * 消息 ID
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
     * 发送者昵称
     */
    private String senderNickname;

    /**
     * 发送者头像
     */
    private String senderAvatar;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 弹幕颜色 1-白色 2-红色 3-蓝色 4-绿色等
     */
    private Integer colorType;

    /**
     * 弹幕类型 1-普通弹幕 2-高级弹幕 3-彩色弹幕
     */
    private Integer messageType;

    /**
     * 用户等级
     */
    private Integer userLevel;

    /**
     * 粉丝牌等级
     */
    private Integer fanMedalLevel;

    /**
     * 粉丝牌名称
     */
    private String fanMedalName;

    /**
     * 是否主播发送 0-否 1-是
     */
    private Integer isAnchor;

    /**
     * 是否管理员发送 0-否 1-是
     */
    private Integer isAdmin;

    /**
     * 是否置顶 0-否 1-是
     */
    private Integer isTop;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 消息状态 0-待审核 1-正常 2-违规
     */
    private Integer status;

    /**
     * 发送时间
     */
    private Long sendTime;

    /**
     * 创建时间
     */
    private Long createTime;
}
