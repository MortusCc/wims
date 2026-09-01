package org.software.productclient.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.software.productclient.client.ProductServiceClient;
import org.software.productclient.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品服务客户端控制层 —— 指导书 2.4.4.4
 * 作为客户端的入口：接收外部请求，再通过 Feign 调用 product-service
 *
 * @AllArgsConstructor 注入 Feign 客户端（final 字段由构造器赋值，无需再 @Autowired）
 * @RestController     标识 REST 控制器
 * @Slf4j             Lombok：自动生成 log 日志对象
 */
@AllArgsConstructor
@RestController
@Slf4j
public class ProductServiceClientController {

    private final ProductServiceClient prodServiceClient;

    /**
     * 根据商品 id 查询商品（转发到 product-service）
     */
    @GetMapping("/findByProductId/{productId}")
    public Product findByProductId(@PathVariable Long productId) {
        Product product = prodServiceClient.findByProductId(productId);
        log.info("-------------In client  findByProductId---------------");
        return product;
    }

    /**
     * 查询所有商品（转发到 product-service）
     */
    @GetMapping("/queryAllProduct")
    public List<Product> queryAllProduct() {
        List<Product> productList = prodServiceClient.queryAllProduct();
        log.info("--------------In client  queryAllProduct--------------");
        return productList;
    }
}
