package com.wen.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wen.module.user.common.FollowTypeEnum;
import com.wen.module.user.mapper.UserFollowMapper;
import com.wen.module.user.mapper.UserRoleMapper;
import com.wen.module.user.model.dto.FollowUserRequest;
import com.wen.module.user.model.entity.UserFollow;
import com.wen.module.user.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author : 青灯文案
 * @Date: 2026/3/16 19:57
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final UserFollowMapper userFollowMapper;

    private final UserRoleMapper userRoleMapper;

    /**
     * 关注某用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String followUser(FollowUserRequest request) {
        UserFollow existingFollow = getUserFollow(request.getUserId(), request.getFollowId());

        if (existingFollow != null) {
            log.info("用户曾关注该用户: {}", existingFollow);
            LambdaUpdateWrapper<UserFollow> updateWrapper = new LambdaUpdateWrapper<UserFollow>()
                    .eq(UserFollow::getUserId, request.getUserId())
                    .eq(UserFollow::getFollowId, request.getFollowId())
                    .set(UserFollow::getIsFollow, FollowTypeEnum.FOLLOW.getCode())
                    .set(UserFollow::getFollowTime, System.currentTimeMillis())
                    .set(UserFollow::getUpdateTime, System.currentTimeMillis());
            userFollowMapper.update(null, updateWrapper);
            return "更新关注状态成功";
        } else {
            log.info("用户未曾关注该用户");
            UserFollow userFollow = new UserFollow();
            userFollow.setUserId(request.getUserId());
            userFollow.setFollowId(request.getFollowId());
            userFollow.setIsFollow(FollowTypeEnum.FOLLOW.getCode());
            userFollow.setFollowTime(System.currentTimeMillis());
            userFollow.setCreateTime(System.currentTimeMillis());
            userFollow.setUpdateTime(System.currentTimeMillis());
            userFollowMapper.insert(userFollow);
            return "关注成功";
        }
    }

    /**
     * 取消关注某用户
     */
    @Override
    public String unfollowUser(FollowUserRequest request) {
        UserFollow existingFollow = getUserFollow(request.getUserId(), request.getFollowId());
        if (existingFollow != null) {
            log.info("用户曾关注该用户: {}", existingFollow);
            LambdaUpdateWrapper<UserFollow> updateWrapper = new LambdaUpdateWrapper<UserFollow>()
                    .eq(UserFollow::getUserId, request.getUserId())
                    .eq(UserFollow::getFollowId, request.getFollowId())
                    .set(UserFollow::getIsFollow, FollowTypeEnum.UNFOLLOW.getCode())
                    .set(UserFollow::getFollowTime, System.currentTimeMillis())
                    .set(UserFollow::getUpdateTime, System.currentTimeMillis());
            userFollowMapper.update(null, updateWrapper);
            return "取消关注状态成功";
        } else {
            log.info("用户未曾关注该用户");
            return "用户未曾关注该用户";
        }
    }

    private UserFollow getUserFollow(Long userId, Long followId) {
        LambdaQueryWrapper<UserFollow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFollow::getUserId, userId);
        queryWrapper.eq(UserFollow::getFollowId, followId);
        return userFollowMapper.selectOne(queryWrapper);
    }

    /**
     * 获取某用户的关注列表
     */
    @Override
    public List<UserFollow> getUserFollowingList(Long userId) {
        LambdaQueryWrapper<UserFollow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFollow::getUserId, userId);
        return userFollowMapper.selectList(queryWrapper);
    }

    /**
     * 获取某用户的粉丝列表
     */
    @Override
    public List<UserFollow> getUserFollowersList(Long userId) {
        LambdaQueryWrapper<UserFollow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFollow::getFollowId, userId);
        return userFollowMapper.selectList(queryWrapper);
    }

}
