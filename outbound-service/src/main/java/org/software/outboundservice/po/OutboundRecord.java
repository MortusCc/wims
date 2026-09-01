package org.software.outboundservice.po;

import lombok.Data;

import java.util.Date;

/**
 * 出库单实体 —— 对应 wims_outbound 库的 outbound_record 表
 * 记录一笔出库业务：单号、商品、数量、经办人、时间
 */
@Data
public class OutboundRecord {

    /** 主键 id */
    private Long id;
    /** 出库单号（唯一），如 CK20260801001 */
    private String billNo;
    /** 商品id */
    private Long productId;
    /** 商品名称（快照） */
    private String productName;
    /** 出库数量 */
    private Integer quantity;
    /** 经办人 */
    private String operator;
    /** 出库时间 */
    private Date outboundTime;
}
