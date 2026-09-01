package org.software.inboundservice.client;

import org.software.inboundservice.dto.Result;
import org.software.inboundservice.dto.StockChangeRequest;
import org.software.inboundservice.dto.StockDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 库存服务 Feign 客户端 —— 入库服务调用库存服务的接口声明
 * name 取值 registry 中的服务名（inventory-service），由 Eureka 服务发现，Ribbon 负载均衡
 */
@FeignClient(name = "inventory-service")
public interface InventoryClient {

    /**
     * 查询该商品当前库存档案（存在则说明库房有该商品，可入库）
     */
    @GetMapping("/stock/{productId}")
    StockDTO getStock(@PathVariable("productId") Long productId);

    /**
     * 通知库存服务增加库存
     */
    @PostMapping("/stock/increase")
    Result increase(@RequestBody StockChangeRequest request);
}
