package com.wen.user.controller;

import com.wen.common.exception.BusinessException;
import com.wen.common.model.response.Response;
import com.wen.common.model.user.AnchorInfoDto;
import com.wen.common.model.user.UserInfoDto;
import com.wen.user.service.AnchorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : rjw
 * @date : 2026-04-08
 */
@RestController
@RequestMapping("/anchor")
@RequiredArgsConstructor
public class AnchorController {

    private final AnchorService anchorService;

    @PostMapping("/register")
    public Response<String> registerAnchor(@RequestBody AnchorInfoDto anchorInfoDto) {
        String response = anchorService.registerAnchor(anchorInfoDto);
        return Response.success(response);
    }

    @GetMapping("/query")
    public Response<List<AnchorInfoDto>> queryAnchor() {
        List<AnchorInfoDto> response = anchorService.queryAnchor();
        return Response.success(response);
    }



}
