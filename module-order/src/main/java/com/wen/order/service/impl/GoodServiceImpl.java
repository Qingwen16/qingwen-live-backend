package com.wen.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wen.common.constant.GoodStatusEnum;
import com.wen.common.model.order.GoodInfoDto;
import com.wen.order.common.InsertGoodRequest;
import com.wen.order.entity.GoodInfo;
import com.wen.order.mapper.GoodInfoMapper;
import com.wen.order.service.GoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : rjw
 * @date : 2026-04-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoodServiceImpl implements GoodService {

    private final GoodInfoMapper goodInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertGood(InsertGoodRequest request) {
        log.info("新增货物信息 - 货物名称: [{}]", request.getName());
        // 检查货物编号是否已存在
        if (checkGoodExists(request.getNumber())) {
            log.error("货物编号已存在: [{}]", request.getNumber());
            return "货物编号已存在";
        }
        // 转换DTO为实体
        GoodInfo goodInfo = new GoodInfo();
        BeanUtils.copyProperties(request, goodInfo);
        // 设置创建时间和更新时间
        long currentTime = System.currentTimeMillis();
        goodInfo.setCreateTime(currentTime);
        goodInfo.setUpdateTime(currentTime);
        goodInfo.setStatus(GoodStatusEnum.NOT_LISTED.getCode());
        goodInfo.setSalesCount(0);
        // 插入数据库
        goodInfoMapper.insert(goodInfo);
        log.info("新增货物成功 - 货物ID: [{}]", goodInfo.getId());
        return "新增货物成功";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateGood(GoodInfoDto request) {
        log.info("更新货物信息 - 货物ID: [{}]", request.getNumber());
        // 检查货物编号是否已存在
        if (!checkGoodExists(request.getNumber())) {
            log.error("未查询到货物编号: [{}]", request.getNumber());
            return "未查询到货物编号";
        }
        GoodInfo goodInfo = new GoodInfo();
        BeanUtils.copyProperties(request, goodInfo);
        goodInfo.setUpdateTime(System.currentTimeMillis());
        // 更新数据库
        goodInfoMapper.updateById(goodInfo);
        log.info("更新货物成功，货物ID：{}", goodInfo.getId());
        return "更新货物成功";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteGood(Long goodId) {
        goodInfoMapper.deleteById(goodId);
        log.info("删除货物成功，货物ID：{}", goodId);
        return "删除货物成功";
    }

    /**
     * 检查货物编号是否已存在
     */
    private boolean checkGoodExists(String number) {
        LambdaQueryWrapper<GoodInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GoodInfo::getNumber, number);
        return goodInfoMapper.selectCount(wrapper) > 0;
    }
}