package org.software.statisticsservice.po;

import lombok.Data;

/**
 * 日报汇总实体 —— 对应 wims_statistics 库的 daily_summary 表
 * 每个商品每天一行：当日入库量 / 出库量 / 库存数量
 */
@Data
public class DailySummary {

    /** 主键 id（写入时无需设置） */
    private Long id;
    /** 统计日期，如 2026-09-01 */
    private String statDate;
    /** 商品id */
    private Long productId;
    /** 商品名称 */
    private String productName;
    /** 当日入库数量 */
    private Integer inQty;
    /** 当日出库数量 */
    private Integer outQty;
    /** 当日库存数量 */
    private Integer stockQty;
}
