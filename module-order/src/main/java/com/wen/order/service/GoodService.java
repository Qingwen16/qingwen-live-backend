package com.wen.order.service;

import com.wen.common.model.order.GoodInfoDto;
import com.wen.order.common.InsertGoodRequest;

/**
 * @author : rjw
 * @date : 2026-04-08
 */
public interface GoodService {

    String insertGood(InsertGoodRequest request);

    String updateGood(GoodInfoDto request);

    String deleteGood(Long goodId);
}
