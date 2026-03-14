package com.wen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wen.model.entity.SMSCode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author : 青灯文案
 * @Date: 2026/3/14 17:25
 * 手机号验证码 Mapper
 */
@Mapper
public interface SMSCodeMapper extends BaseMapper<SMSCode> {
}
