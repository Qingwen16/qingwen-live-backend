package com.wen.auth.common;

import com.wen.auth.service.LoginService;
import com.wen.common.enums.auth.LoginTypeEnum;
import com.wen.common.exception.BusinessException;
import com.wen.common.vo.auth.LoginRequest;
import com.wen.common.vo.user.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 登录工厂类
 * 根据登录类型分发到对应的处理器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginFactory {

    private final List<LoginService> loginHandlers;

    /**
     * 缓存处理器映射关系
     */
    private Map<LoginTypeEnum, LoginService> handlerMap;

    /**
     * 初始化处理器映射
     */
    private void initHandlerMap() {
        if (handlerMap == null) {
            handlerMap = loginHandlers.stream()
                    .collect(Collectors.toMap(
                            LoginService::getSupportedType,
                            Function.identity()
                    ));
        }
    }

    /**
     * 执行登录
     * @param request 登录请求
     * @return 用户信息
     */
    public UserInfoResponse executeLogin(LoginRequest request) {
        initHandlerMap();

        LoginTypeEnum loginTypeEnum = request.getLoginTypeEnum();
        if (loginTypeEnum == null) {
            throw new BusinessException("登录类型不能为空");
        }

        LoginService service = handlerMap.get(loginTypeEnum);
        if (service == null) {
            throw new BusinessException("暂不支持该登录方式：" + loginTypeEnum);
        }

        log.info("执行登录，类型：{}", loginTypeEnum);
        return service.login(request);
    }

    /**
     * 获取所有支持的登录方式
     */
    public List<LoginTypeEnum> getSupportedTypes() {
        initHandlerMap();
        return new ArrayList<>(handlerMap.keySet());
    }
}


