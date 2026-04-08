package com.wen.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单信息实体类
 * @author : rjw
 * @date : 2026-04-08
 */
@Data
@TableName("order_info")
public class OrderInfo {
    /**
     * 订单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品价格
     */
    private BigDecimal productPrice;
    /**
     * 购买数量
     */
    private Integer quantity;
    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;
    /**
     * 订单状态：0-待支付，1-已支付，2-已取消，3-已完成
     */
    private Integer status;
    /**
     * 支付方式：1-支付宝，2-微信，3-银行卡
     */
    private Integer payType;
    /**
     * 支付时间
     */
    private Long payTime;
    /**
     * 收货地址
     */
    private String address;
    /**
     * 收货人姓名
     */
    private String receiverName;
    /**
     * 收货人电话
     */
    private String receiverPhone;
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