package org.software.productclient.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.software.productclient.model.Product;

import java.util.List;

/**
 * 商品服务远程调用接口（Feign）—— 指导书 2.4.4.3 / 2.6.1（熔断）
 *
 * @FeignClient 的 name 必须等于被调用服务在 Eureka 注册的名称（product-service），
 * Feign 据此通过 Eureka + Ribbon 找到服务实例并发起 HTTP 调用。
 * fallback 指定降级处理类（指导书 2.6）：远程调用超时/失败时自动触发该类中的对应方法
 */
@FeignClient(name = "product-service", fallback = ProductServiceFallback.class)
public interface ProductServiceClient {

    /**
     * 根据商品 id 获取商品对象
     */
    @GetMapping("/findByProductId/{productId}")
    Product findByProductId(@PathVariable("productId") Long productId);

    /**
     * 获取所有商品集合
     */
    @GetMapping("/queryAllProduct")
    List<Product> queryAllProduct();
}
