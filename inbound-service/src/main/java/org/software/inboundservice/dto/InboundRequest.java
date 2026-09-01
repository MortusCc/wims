package org.software.inboundservice.dto;

import lombok.Data;

/**
 * 入库请求参数：哪个商品入库、入库多少、谁经办的
 */
@Data
public class InboundRequest {

    /** 商品id */
    private Long productId;
    /** 入库数量 */
    private Integer quantity;
    /** 经办人 */
    private String operator;
}
