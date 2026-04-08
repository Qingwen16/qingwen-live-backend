package com.wen.user.service;

import com.wen.common.model.user.AnchorInfoDto;
import com.wen.user.entity.AnchorInfo;

import java.util.List;

/**
 * @author : rjw
 * @date : 2026-04-08
 */
public interface AnchorService {

    /**
     * 注册主播
     */
    String registerAnchor(AnchorInfoDto anchorInfoDto);

    /**
     * 根据用户ID查询主播信息
     */
    AnchorInfo queryAnchorByUserId(Long userId);

    /**
     * 批量查询主播信息
     */
    List<AnchorInfoDto> queryAnchor();

}
