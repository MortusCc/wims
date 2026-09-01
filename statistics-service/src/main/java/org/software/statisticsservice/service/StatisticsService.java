package org.software.statisticsservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.software.statisticsservice.client.InboundClient;
import org.software.statisticsservice.client.InventoryClient;
import org.software.statisticsservice.client.OutboundClient;
import org.software.statisticsservice.dto.InboundDTO;
import org.software.statisticsservice.dto.OutboundDTO;
import org.software.statisticsservice.dto.StockDTO;
import org.software.statisticsservice.mapper.DailySummaryMapper;
import org.software.statisticsservice.po.DailySummary;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存统计业务逻辑 —— 微服务间协作链路示例（Feign 聚合三个服务的数据）
 * 统计流程：① 拉取入库服务当日入库单 ② 拉取出库服务当日出库单 ③ 拉取库存服务最新库存
 *         ④ 按商品聚合入库量/出库量/库存 → 写入日报表（重复执行会覆盖,不产生脏数据）
 */
@Service
@AllArgsConstructor
@Slf4j
public class StatisticsService {

    private final InboundClient inboundClient;
    private final OutboundClient outboundClient;
    private final InventoryClient inventoryClient;
    private final DailySummaryMapper dailySummaryMapper;

    /**
     * 生成并返回当日日报
     */
    public List<DailySummary> dailyReport() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // 按商品聚合：key=productId
        Map<Long, DailySummary> map = new LinkedHashMap<>();

        // ① 当日入库量
        List<InboundDTO> inbounds = inboundClient.listInbound();
        for (InboundDTO dto : inbounds) {
            if (dto.getInboundTime() != null && today.equals(timeToDateStr(dto.getInboundTime()))) {
                DailySummary s = summary(map, dto.getProductId(), dto.getProductName());
                s.setInQty(s.getInQty() + dto.getQuantity());
            }
        }

        // ② 当日出库量
        List<OutboundDTO> outbounds = outboundClient.listOutbound();
        for (OutboundDTO dto : outbounds) {
            if (dto.getOutboundTime() != null && today.equals(timeToDateStr(dto.getOutboundTime()))) {
                DailySummary s = summary(map, dto.getProductId(), dto.getProductName());
                s.setOutQty(s.getOutQty() + dto.getQuantity());
            }
        }

        // ③ 当日库存（拿最新库存台账作为期末库存）
        List<StockDTO> stocks = inventoryClient.listStock();
        for (StockDTO dto : stocks) {
            DailySummary s = summary(map, dto.getProductId(), dto.getProductName());
            s.setProductName(dto.getProductName());
            s.setStockQty(dto.getQuantity());
        }

        // ④ 写入日报表（数据库唯一键 stat_date+product_id,重复执行自动覆盖）
        for (DailySummary s : map.values()) {
            s.setStatDate(today);
            dailySummaryMapper.insertOrUpdate(s);
            log.info("日报写入：{} 商品={} 入库={} 出库={} 库存={}", today, s.getProductName(),
                    s.getInQty(), s.getOutQty(), s.getStockQty());
        }
        log.info("-------------OK   /statistics/daily--------------------");
        return dailySummaryMapper.queryByDate(today);
    }

    /**
     * 取聚合条目：不存在则初始化（入库/出库/库存三路数据共用一个商品条目）
     */
    private DailySummary summary(Map<Long, DailySummary> map, Long productId, String productName) {
        DailySummary s = map.get(productId);
        if (s == null) {
            s = new DailySummary();
            s.setProductId(productId);
            s.setProductName(productName);
            s.setInQty(0);
            s.setOutQty(0);
            s.setStockQty(0);
            map.put(productId, s);
        }
        return s;
    }

    /**
     * Date → 日期字符串（yyyy-MM-dd）
     */
    private String timeToDateStr(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
