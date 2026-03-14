package com.wen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * WebSocket 连接记录表
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@TableName("websocket_session")
@Data
public class WebSocketSession {

    /**
     * 记录 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * Session ID
     */
    private String sessionId;

    /**
     * 连接的房间 ID
     */
    private Long roomId;

    /**
     * 客户端 IP
     */
    private String clientIp;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 连接时间
     */
    private Long connectTime;

    /**
     * 最后活跃时间
     */
    private Long lastActiveTime;

    /**
     * 断开时间
     */
    private Long disconnectTime;

    /**
     * 连接状态 0-已断开 1-连接中
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long createTime;
}
