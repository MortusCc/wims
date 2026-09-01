package org.software.statisticsservice.client;

import org.software.statisticsservice.dto.OutboundDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 出库服务 Feign 客户端 —— 拉取出库单用于统计当日出库量
 */
@FeignClient(name = "outbound-service")
public interface OutboundClient {

    /**
     * 拉取全部出库单
     */
    @GetMapping("/outbound/list")
    List<OutboundDTO> listOutbound();
}
