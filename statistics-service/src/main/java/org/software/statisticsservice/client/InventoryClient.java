package org.software.statisticsservice.client;

import org.software.statisticsservice.dto.StockDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 库存服务 Feign 客户端 —— 拉取最新库存用于报表的“当日库存”
 */
@FeignClient(name = "inventory-service")
public interface InventoryClient {

    /**
     * 拉取全部库存台账
     */
    @GetMapping("/stock/list")
    List<StockDTO> listStock();
}
