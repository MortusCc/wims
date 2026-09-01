package org.software.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库存增减业务参数：谁的商品、变动多少数量（入库自动加，出库自动减）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockChangeRequest {

    /** 商品id */
    private Long productId;
    /** 变动数量（正数，增减方向由接口决定） */
    private Integer quantity;
}
