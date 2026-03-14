package com.wen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 礼物赠送记录表
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@TableName("gift_record")
@Data
public class GiftRecord {

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
     * 主播 ID
     */
    private Long anchorId;

    /**
     * 赠送用户 ID
     */
    private Long userId;

    /**
     * 赠送者昵称
     */
    private String senderNickname;

    /**
     * 赠送者头像
     */
    private String senderAvatar;

    /**
     * 礼物 ID
     */
    private Long giftId;

    /**
     * 礼物名称
     */
    private String giftName;

    /**
     * 礼物图片
     */
    private String giftImage;

    /**
     * 礼物单价
     */
    private Long giftPrice;

    /**
     * 赠送数量
     */
    private Integer giftCount;

    /**
     * 总价值
     */
    private Long totalPrice;

    /**
     * 礼物消息内容（自定义消息）
     */
    private String message;

    /**
     * 连击次数
     */
    private Integer comboCount;

    /**
     * 是否全服广播 0-否 1-是
     */
    private Integer isBroadcast;

    /**
     * 赠送时间
     */
    private Long sendTime;

    /**
     * 创建时间
     */
    private Long createTime;
}
