package com.wen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 充值记录表
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@TableName("recharge_record")
@Data
public class RechargeRecord {

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
     * 订单号
     */
    private String orderNo;

    /**
     * 充值金额（分）
     */
    private Long amount;

    /**
     * 获得虚拟币数量
     */
    private Long coins;

    /**
     * 充值渠道 1-微信 2-支付宝 3-银行卡等
     */
    private Integer channel;

    /**
     * 充值类型 1-手动充值 2-自动充值
     */
    private Integer rechargeType;

    /**
     * 支付状态 0-待支付 1-支付成功 2-支付失败 3-已取消
     */
    private Integer payStatus;

    /**
     * 第三方支付订单号
     */
    private String thirdPartyOrderNo;

    /**
     * 支付时间
     */
    private Long payTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;
}
