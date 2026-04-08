package com.wen.order.controller;

import com.wen.common.model.order.GoodInfoDto;
import com.wen.common.model.response.Response;
import com.wen.order.common.InsertGoodRequest;
import com.wen.order.service.GoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

/**
 * @author : rjw
 * @date : 2026-04-08
 */
@RestController
@RequestMapping("/good")
@RequiredArgsConstructor
public class GoodController {

    private final GoodService goodService;

    /**
     * 新增商品
     */
    @PostMapping("/insert/good")
    public Response<String> insertGood(@RequestBody InsertGoodRequest request) {
        String response = goodService.insertGood(request);
        return Response.success(response);
    }

    /**
     * 新增商品
     */
    @PostMapping("/update/good")
    public Response<String> updateGood(@RequestBody GoodInfoDto request) {
        String response = goodService.updateGood(request);
        return Response.success(response);
    }

    /**
     * 删除商品
     */
    @GetMapping("/delete/good")
    public Response<String> deleteGood(@Param("goodId") Long goodId) {
        String response = goodService.deleteGood(goodId);
        return Response.success(response);
    }

}
