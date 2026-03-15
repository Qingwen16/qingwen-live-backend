package com.wen.module.user.common.constant;

/**
 * 用户状态枚举
 */
public enum UserStatus {

    /**
     * 禁用（封号、违规等）
     */
    DISABLED(0, "禁用"),

    /**
     * 正常（可正常使用所有功能）
     */
    NORMAL(1, "正常");

    private final int code;
    private final String description;

    UserStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据 code 获取枚举
     */
    public static UserStatus fromCode(int code) {
        for (UserStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的用户状态：" + code);
    }
}
