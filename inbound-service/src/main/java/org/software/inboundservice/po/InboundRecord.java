package org.software.inboundservice.po;

import lombok.Data;

import java.util.Date;

/**
 * 入库单实体 —— 对应 wims_inbound 库的 inbound_record 表
 * 记录一笔入库业务：单号、商品、数量、经办人、时间
 */
@Data
public class InboundRecord {

    /** 主键 id */
    private Long id;
    /** 入库单号（唯一），如 RK20260801001 */
    private String billNo;
    /** 商品id */
    private Long productId;
    /** 商品名称（快照） */
    private String productName;
    /** 入库数量 */
    private Integer quantity;
    /** 经办人 */
    private String operator;
    /** 入库时间 */
    private Date inboundTime;
}
