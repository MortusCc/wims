package org.software.outboundservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.software.outboundservice.client.InventoryClient;
import org.software.outboundservice.dto.OutboundRequest;
import org.software.outboundservice.dto.Result;
import org.software.outboundservice.dto.StockChangeRequest;
import org.software.outboundservice.dto.StockDTO;
import org.software.outboundservice.mapper.OutboundRecordMapper;
import org.software.outboundservice.po.OutboundRecord;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 出库业务逻辑 —— 微服务间协作链路示例（Feign 调用库存服务）
 * 出库流程：① 校验库存服务中存在该商品档案 ② 通知库存服务扣减库存（不足则拦截） ③ 扣减成功才保存出库单
 */
@Service
@AllArgsConstructor
@Slf4j
public class OutboundService {

    private final OutboundRecordMapper outboundRecordMapper;
    private final InventoryClient inventoryClient;

    /**
     * 执行出库
     */
    public Result outbound(OutboundRequest request) {
        // ① 校验库存档案：库存目录中没有该商品则不允许出库
        StockDTO stock = inventoryClient.getStock(request.getProductId());
        if (stock == null) {
            return new Result(false, "库房无该商品档案，无法出库");
        }

        // ② 通知库存服务扣减库存：库存不足时入库服务返回 success=false（如"库存不足，当前库存=50"）
        Result decrease = inventoryClient.decrease(
                new StockChangeRequest(request.getProductId(), request.getQuantity()));
        if (!decrease.isSuccess()) {
            log.warn("出库被拦截：{}", decrease.getMessage());
            return decrease;
        }

        // ③ 扣减成功，保存出库单（CK + 时间 + 随机3位）
        OutboundRecord record = new OutboundRecord();
        record.setBillNo(genBillNo("CK"));
        record.setProductId(request.getProductId());
        record.setProductName(stock.getProductName());
        record.setQuantity(request.getQuantity());
        record.setOperator(request.getOperator());
        record.setOutboundTime(new Date());
        outboundRecordMapper.insert(record);
        log.info("出库单已登记：{} 商品={} 数量={}", record.getBillNo(), record.getProductName(), record.getQuantity());

        return new Result(true, "出库成功，单号=" + record.getBillNo());
    }

    /**
     * 生成业务单号：前缀 + 时间戳(yyyyMMddHHmmss) + 随机3位，保证唯一
     */
    private String genBillNo(String prefix) {
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String suffix = String.format("%03d", new Random().nextInt(1000));
        return prefix + time + suffix;
    }
}
