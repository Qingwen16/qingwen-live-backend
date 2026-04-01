package com.wen.auth.common;

import com.wen.common.exception.BusinessException;
import com.wen.module.auth.model.dto.LoginRequest;
import com.wen.module.user.model.dto.UserInfoResponse;
import com.wen.module.auth.service.LoginHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private final List<LoginHandler> loginHandlers;

    /**
     * 缓存处理器映射关系
     */
    private Map<AuthTypeEnum, LoginHandler> handlerMap;

    /**
     * 初始化处理器映射
     */
    private void initHandlerMap() {
        if (handlerMap == null) {
            handlerMap = loginHandlers.stream()
                    .collect(Collectors.toMap(
                            LoginHandler::getSupportedType,
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

        AuthTypeEnum authTypeEnum = request.getAuthTypeEnum();
        if (authTypeEnum == null) {
            throw new BusinessException("登录类型不能为空");
        }

        LoginHandler handler = handlerMap.get(authTypeEnum);
        if (handler == null) {
            throw new BusinessException("暂不支持该登录方式：" + authTypeEnum);
        }

        log.info("执行登录，类型：{}", authTypeEnum);
        return handler.login(request);
    }

    /**
     * 获取所有支持的登录方式
     */
    public List<AuthTypeEnum> getSupportedTypes() {
        initHandlerMap();
        return new ArrayList<>(handlerMap.keySet());
    }
}


