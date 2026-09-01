package org.software.statisticsservice.dto;

import lombok.Data;

import java.util.Date;

/**
 * 入库单 DTO（Feign 反序列化用）—— 与 inbound-service 中 InboundRecord 的 JSON 字段一致
 */
@Data
public class InboundDTO {

    /** 商品id */
    private Long productId;
    /** 商品名称（快照） */
    private String productName;
    /** 入库数量 */
    private Integer quantity;
    /** 入库时间 */
    private Date inboundTime;
}
