package org.software.outboundservice.dto;

import lombok.Data;

/**
 * 出库请求参数：哪个商品出库、出库多少、谁经办的
 */
@Data
public class OutboundRequest {

    /** 商品id */
    private Long productId;
    /** 出库数量 */
    private Integer quantity;
    /** 经办人 */
    private String operator;
}
