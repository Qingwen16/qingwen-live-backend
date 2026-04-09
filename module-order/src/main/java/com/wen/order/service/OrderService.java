package com.wen.order.service;

import com.wen.order.common.OrderCreateRequest;
import com.wen.order.common.OrderQueryRequest;

/**
 * @author : rjw
 * @date : 2026-04-08
 */
public interface OrderService {

    String createOrder(OrderCreateRequest request);

    String queryOrder(OrderQueryRequest request);

}
