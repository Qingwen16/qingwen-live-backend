package com.wen.model.dto;

import lombok.Data;

/**
 * 地址更新请求 DTO
 */
@Data
public class AddressUpdateRequest {

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 邮政编码
     */
    private String zipCode;
}
