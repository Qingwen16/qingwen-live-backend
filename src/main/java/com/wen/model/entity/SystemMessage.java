package com.wen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 系统消息表
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@TableName("system_message")
@Data
public class SystemMessage {

    /**
     * 消息 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 接收用户 ID（0 表示全员）
     */
    private Long userId;

    /**
     * 消息类型 1-系统通知 2-活动消息 3-警告消息 4-私信
     */
    private Integer messageType;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息链接
     */
    private String linkUrl;

    /**
     * 是否已读 0-未读 1-已读
     */
    private Integer isRead;

    /**
     * 阅读时间
     */
    private Long readTime;

    /**
     * 发送者 ID（0 表示系统）
     */
    private Long senderId;

    /**
     * 发送时间
     */
    private Long sendTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 创建时间
     */
    private Long createTime;
}
