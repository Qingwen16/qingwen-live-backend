package com.wen.room.controller;


import com.wen.common.model.response.Response;
import com.wen.common.model.user.UserInfoResponse;
import com.wen.room.common.RoomIdRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : rjw
 * @date : 2026-03-16
 */
@RestController
@RequestMapping("/live")
@RequiredArgsConstructor
public class LiveRoomController {


    /**
     * 创建直播间（一个人只能创建一个）
     */
    @PostMapping("/createLiveRoom")
    public Response<UserInfoResponse> createLiveRoom(@RequestBody RoomIdRequest request) {
        return Response.success();
    }

    /**
     * 修改直播间
     */
    @PostMapping("/updateLiveRoom")
    public Response<UserInfoResponse> updateLiveRoom(@RequestBody RoomIdRequest request) {
        return Response.success();
    }

    /**
     * 主播获得推流地址
     */
    @PostMapping("/getStreamKey")
    public Response<UserInfoResponse> getStreamKey(@RequestBody RoomIdRequest request) {
        return Response.success();
    }

    /**
     * 用户获得直播列表
     */
    @PostMapping("/getLiveRoomList")
    public Response<UserInfoResponse> getLiveRoomList(@RequestBody RoomIdRequest request) {
        return Response.success();
    }

    /**
     * 用户获得单个直播间的信息
     */
    @PostMapping("/getLiveRoomInfo")
    public Response<UserInfoResponse> getLiveRoomInfo(@RequestBody RoomIdRequest request) {
        return Response.success();
    }

    /**
     * 接收阿里云回调
     */
    @PostMapping("/callbacks/aliyun")
    public Response<UserInfoResponse> callbacksAliyun(@RequestBody RoomIdRequest request) {
        return Response.success();
    }


}
