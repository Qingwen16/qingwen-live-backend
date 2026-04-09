package com.wen.order.service;

import com.wen.common.model.order.GoodInfoDto;
import com.wen.order.common.GoodCreateRequest;
import com.wen.order.common.GoodUpdateRequest;
import com.wen.order.entity.GoodInfo;

import java.util.List;

/**
 * @author : rjw
 * @date : 2026-04-08
 */
public interface GoodService {

    /**
     * 新增商品
     */
    String createGood(GoodCreateRequest request);

    /**
     * 修改商品
     */
    String updateGood(GoodUpdateRequest request);

    /**
     * 删除商品
     */
    String deleteGood(Long goodId);

    /**
     * 获取所有商品
     */
    GoodInfo queryGoodById(Long goodId);

    /**
     * 获取所有商品
     */
    List<GoodInfoDto> queryTotalGoods();

    /**
     * 获取所有上架商品列表
     */
    List<GoodInfoDto> queryTotalListedGoods();

    /**
     * 扣减库存
     */
    boolean reduceGoodStock(Long goodId, Integer quantity);
}
