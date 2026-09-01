package org.software.statisticsservice.dto;

import lombok.Data;

import java.util.Date;

/**
 * 出库单 DTO（Feign 反序列化用）—— 与 outbound-service 中 OutboundRecord 的 JSON 字段一致
 */
@Data
public class OutboundDTO {

    /** 商品id */
    private Long productId;
    /** 商品名称（快照） */
    private String productName;
    /** 出库数量 */
    private Integer quantity;
    /** 出库时间 */
    private Date outboundTime;
}
