package org.software.inventoryservice.po;

import lombok.Data;

import java.util.Date;

/**
 * 库存台账实体 —— 对应 wims_inventory 库的 stock 表
 * 一个商品一条库存记录；safe_stock 为安全库存阈值，低于该值触发“库存不足”预警
 */
@Data
public class Stock {

    /** 主键 id */
    private Long id;
    /** 商品id（对应 wims_product.product.id） */
    private Long productId;
    /** 商品名称（冗余，便于查询展示） */
    private String productName;
    /** 当前库存数量 */
    private Integer quantity;
    /** 安全库存阈值 */
    private Integer safeStock;
    /** 最近更新时间 */
    private Date updateTime;
}
