package org.software.productservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.software.productservice.mapper.ProductMapper;
import org.software.productservice.po.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品服务控制层 —— 指导书 2.4.3.4
 * 接收外部 HTTP 请求，调用 Mapper 访问数据库，返回结果
 *
 * @AllArgsConstructor Lombok：自动生成含全部字段的构造器，便于注入 final 依赖
 * @RestController     标识 REST 控制器，方法返回值直接写入响应体
 * @Slf4j             Lombok：自动生成名为 log 的日志对象
 */
@AllArgsConstructor
@RestController
@Slf4j
public class ProductController {

    private final ProductMapper productMapper;

    /**
     * 根据商品 id 查询商品
     */
    @GetMapping("/findByProductId/{productId}")
    public Product findByProductId(@PathVariable Long productId) {
        Product product = productMapper.findByProductId(productId);
        log.info("-------------OK   /findByProductId/{productId}--------------------");
        return product;
    }

    /**
     * 查询所有商品
     */
    @GetMapping("/queryAllProduct")
    public List<Product> queryAllProduct() {
        List<Product> productList = productMapper.queryAllProduct();
        log.info("-------------OK   queryAllProduct--------------------");
        return productList;
    }
}
