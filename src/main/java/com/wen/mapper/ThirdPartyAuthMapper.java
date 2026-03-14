package com.wen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wen.model.entity.ThirdPartyAuth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 第三方登录 Mapper
 */
@Mapper
public interface ThirdPartyAuthMapper extends BaseMapper<ThirdPartyAuth> {

    /**
     * 根据 OpenID 查询授权信息
     */
    ThirdPartyAuth selectByOpenId(@Param("openId") String openId,
                                   @Param("authType") Integer authType);
}
