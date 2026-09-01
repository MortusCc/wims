package org.software.alertservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.software.alertservice.client.InventoryClient;
import org.software.alertservice.dto.StockDTO;
import org.software.alertservice.mapper.AlertRecordMapper;
import org.software.alertservice.po.AlertRecord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存预警业务逻辑 —— 微服务间协作链路示例（Feign 拉取库存服务数据）
 * 预警规则：库存数量 < 安全库存阈值 → 生成 LOW（库存不足）预警
 * 去重策略：该商品已有同类未处理预警时不重复生成
 */
@Service
@AllArgsConstructor
@Slf4j
public class AlertService {

    private final InventoryClient inventoryClient;
    private final AlertRecordMapper alertRecordMapper;

    /**
     * 执行一次库存预警检查：拉取全量库存 → 与安全阈值比对 → 生成/跳过预警
     *
     * @return 检查结果（检查商品数、本次新增预警数、当前未处理预警总数）
     */
    public Map<String, Object> checkStock() {
        List<StockDTO> stocks = inventoryClient.listStock();
        int newAlerts = 0;
        List<AlertRecord> unhandled = new ArrayList<>();

        for (StockDTO stock : stocks) {
            // 对比安全阈值：低于则预警
            if (stock.getQuantity() != null && stock.getSafeStock() != null
                    && stock.getQuantity() < stock.getSafeStock()) {
                // 去重：已有同类未处理预警则跳过
                if (alertRecordMapper.countUnhandled(stock.getProductId(), "LOW") > 0) {
                    continue;
                }
                AlertRecord record = new AlertRecord();
                record.setProductId(stock.getProductId());
                record.setProductName(stock.getProductName());
                record.setAlertType("LOW");
                record.setCurQuantity(stock.getQuantity());
                record.setSafeStock(stock.getSafeStock());
                record.setAlertTime(new Date());
                record.setStatus(0);
                alertRecordMapper.insert(record);
                newAlerts++;
                log.info("生成库存预警：{} 当前库存={} 安全库存={}", stock.getProductName(),
                        stock.getQuantity(), stock.getSafeStock());
            }
        }

        // 汇总当前未处理预警
        List<AlertRecord> all = alertRecordMapper.queryAll();
        for (AlertRecord r : all) {
            if (r.getStatus() == 0) {
                unhandled.add(r);
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("检查商品数", stocks.size());
        result.put("本次新增预警", newAlerts);
        result.put("未处理预警", unhandled);
        log.info("-------------OK   /alert/check  新增={}--------------------", newAlerts);
        return result;
    }
}
