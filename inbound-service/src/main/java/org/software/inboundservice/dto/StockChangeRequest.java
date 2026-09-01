package org.software.inboundservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库存变动请求（Feign 传参）—— 与 inventory-service 中的 StockChangeRequest 字段一致
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockChangeRequest {

    /** 商品id */
    private Long productId;
    /** 变动数量（正数） */
    private Integer quantity;
}
