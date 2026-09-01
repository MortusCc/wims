package org.software.inventoryservice.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.software.inventoryservice.po.Stock;

import java.util.List;

/**
 * 库存台账数据访问接口（MyBatis 注解方式）
 */
public interface StockMapper {

    /**
     * 根据商品 id 查询库存台账
     */
    @Select("select id, product_id as productId, product_name as productName, quantity, safe_stock as safeStock, update_time as updateTime from stock where product_id = #{productId}")
    Stock findByProductId(@Param("productId") Long productId);

    /**
     * 查询全部库存（供库存一览、预警检查、报表统计使用）
     */
    @Select("select id, product_id as productId, product_name as productName, quantity, safe_stock as safeStock, update_time as updateTime from stock")
    List<Stock> queryAll();

    /**
     * 增加库存：数量直接累加，如 350+100
     */
    @Update("update stock set quantity = quantity + #{quantity}, update_time = now() where product_id = #{productId}")
    int increaseQuantity(@Param("productId") Long productId, @Param("quantity") int quantity);

    /**
     * 扣减库存：where 限定 quantity >= #{quantity}，库存不足时影响行数为 0（防止扣成负数，返回 false 由调用方给出业务提示）
     */
    @Update("update stock set quantity = quantity - #{quantity}, update_time = now() where product_id = #{productId} and quantity >= #{quantity}")
    int decreaseQuantity(@Param("productId") Long productId, @Param("quantity") int quantity);
}
