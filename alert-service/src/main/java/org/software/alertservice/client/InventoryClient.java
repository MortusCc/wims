package org.software.alertservice.client;

import org.software.alertservice.dto.StockDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 库存服务 Feign 客户端 —— 预警服务拉取全量库存的接口声明
 */
@FeignClient(name = "inventory-service")
public interface InventoryClient {

    /**
     * 拉取全部库存台账（含安全阈值），用于比对生成预警
     */
    @GetMapping("/stock/list")
    List<StockDTO> listStock();
}
