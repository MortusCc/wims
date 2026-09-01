package org.software.productclient.model;

import lombok.Data;

/**
 * 商品档案实体（客户端侧/Feign 反序列化 DTO）—— 指导书 2.4.4.5 / WIMS 商品管理
 * 字段与 product-service 中的 Product 保持一致：编码/名称/规格/单位
 */
@Data
public class Product {

    private Long id;
    private String productCode;
    private String productName;
    private String spec;
    private String unit;
}
