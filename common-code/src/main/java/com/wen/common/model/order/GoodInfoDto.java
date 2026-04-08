package com.wen.common.model.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 货物信息实体类
 *
 * @author : rjw
 * @date : 2026-04-08
 */
@Data
public class GoodInfoDto {
    /**
     * 货物编号
     */
    private String number;
    /**
     * 货物名称
     */
    private String name;
    /**
     * 货物描述
     */
    private String desc;
    /**
     * 货物价格
     */
    private BigDecimal price;
    /**
     * 库存数量
     */
    private Integer stock;
    /**
     * 货物单位
     */
    private String unit;
    /**
     * 货物重量(kg)
     */
    private BigDecimal weight;
    /**
     * 货物图片URL
     */
    private String imageUrl;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 货物状态：0-已下架，1-已上架，2-缺货
     */
    private Integer status;
    /**
     * 销售数量
     */
    private Integer salesCount;
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