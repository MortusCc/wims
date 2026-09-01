package org.software.alertservice.po;

import lombok.Data;

import java.util.Date;

/**
 * 库存预警实体 —— 对应 wims_alert 库的 alert_record 表
 * 记录一次库存异常：商品、类型（LOW=库存不足）、触发时数量、阈值、处理状态
 */
@Data
public class AlertRecord {

    /** 主键 id */
    private Long id;
    /** 商品id */
    private Long productId;
    /** 商品名称 */
    private String productName;
    /** 预警类型：LOW=库存不足（低于安全库存） HIGH=库存过剩（预留） */
    private String alertType;
    /** 触发预警时的库存数量 */
    private Integer curQuantity;
    /** 当时的安全库存阈值 */
    private Integer safeStock;
    /** 预警时间 */
    private Date alertTime;
    /** 处理状态：0=未处理 1=已处理 */
    private Integer status;
}
