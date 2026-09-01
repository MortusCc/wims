package org.software.productservice.po;

import lombok.Data;

/**
 * 商品档案实体（WIMS 商品管理微服务）—— 指导书 1.2.1.5 / 2.4.3.6
 * 字段与 wims_product 库的 product 表对应：
 * 库存系统的商品主数据以“编码 + 名称 + 规格 + 单位”为核心，价格不属于库存域（WIMS 无价格字段）
 */
@Data
public class Product {

    /** 主键 id */
    private Long id;
    /** 商品编码（唯一） */
    private String productCode;
    /** 商品名称 */
    private String productName;
    /** 规格描述，如 24瓶/箱 */
    private String spec;
    /** 计量单位，如 箱/包/袋/桶 */
    private String unit;
}
