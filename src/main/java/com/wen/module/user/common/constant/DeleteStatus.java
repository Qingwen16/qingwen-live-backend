package com.wen.module.user.common.constant;

/**
 * 用户删除状态枚举
 */
public enum DeleteStatus {

    /**
     * 已注销（逻辑删除）
     */
    DELETED(0, "已注销"),

    /**
     * 未注销（正常状态）
     */
    ACTIVE(1, "未注销");

    private final int code;
    private final String description;

    DeleteStatus(int code, String description) {
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
    public static DeleteStatus fromCode(int code) {
        for (DeleteStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的删除状态：" + code);
    }
}
