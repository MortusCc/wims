package org.software.outboundservice.dto;

import lombok.Data;

/**
 * 库存台账 DTO（Feign 反序列化用）—— 与 inventory-service 中 Stock 的 JSON 字段一致
 */
@Data
public class StockDTO {

    /** 商品id */
    private Long productId;
    /** 商品名称 */
    private String productName;
    /** 当前库存数量 */
    private Integer quantity;
    /** 安全库存阈值 */
    private Integer safeStock;
}
