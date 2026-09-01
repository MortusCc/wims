package org.software.productservice.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.software.productservice.po.Product;

import java.util.List;

/**
 * 商品数据访问接口（MyBatis）—— 指导书 2.4.3.5
 * 使用注解方式定义 SQL，无需额外的 XML 映射文件
 */
public interface ProductMapper {

    /**
     * 根据商品 id 查询商品
     */
    // 修复指导书笔误：原代码 select 漏了 p.id，导致按 id 查询时 id 字段映射为 null
    // WIMS 版：商品档案为 编码/名称/规格/单位（库存域不含价格）
    @Select("select p.id, p.product_code as productCode, p.product_name as productName, p.spec as spec, p.unit as unit from product p where id = #{productId}")
    Product findByProductId(@Param("productId") Long productId);

    /**
     * 查询所有商品
     */
    @Select("select p.id, p.product_code as productCode, p.product_name as productName, p.spec as spec, p.unit as unit from product p")
    List<Product> queryAllProduct();
}
