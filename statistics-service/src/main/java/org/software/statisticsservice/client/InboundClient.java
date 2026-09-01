package org.software.statisticsservice.client;

import org.software.statisticsservice.dto.InboundDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 入库服务 Feign 客户端 —— 拉取入库单用于统计当日入库量
 */
@FeignClient(name = "inbound-service")
public interface InboundClient {

    /**
     * 拉取全部入库单
     */
    @GetMapping("/inbound/list")
    List<InboundDTO> listInbound();
}
