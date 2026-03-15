package com.wen.module.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户钱包表
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@TableName("user_wallet")
@Data
public class UserWallet {

    /**
     * 钱包 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 账户余额（虚拟币）
     */
    private Long balance;

    /**
     * 充值总额
     */
    private Long totalRecharge;

    /**
     * 消费总额
     */
    private Long totalConsumption;

    /**
     * 收到礼物总额
     */
    private Long totalGiftReceived;

    /**
     * 送出礼物总额
     */
    private Long totalGiftSent;

    /**
     * 积分
     */
    private Long points;

    /**
     * 经验值
     */
    private Long experience;

    /**
     * 用户等级
     */
    private Integer userLevel;

    /**
     * VIP 等级 0-普通 1-VIP1 2-VIP2...
     */
    private Integer vipLevel;

    /**
     * VIP 到期时间
     */
    private Long vipExpireTime;

    /**
     * 上次登录时间
     */
    private Long lastLoginTime;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;
}
