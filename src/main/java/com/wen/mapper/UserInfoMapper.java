package com.wen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wen.model.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户信息 Mapper
 * @Author : 青灯文案
 * @Date: 2026/3/14
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 根据用户名查询用户
     */
    UserInfo selectByUsername(@Param("username") String username);

    /**
     * 根据手机号查询用户
     */
    UserInfo selectByPhone(@Param("phone") String phone);
}
