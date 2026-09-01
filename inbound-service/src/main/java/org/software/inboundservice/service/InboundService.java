package org.software.inboundservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.software.inboundservice.client.InventoryClient;
import org.software.inboundservice.dto.InboundRequest;
import org.software.inboundservice.dto.Result;
import org.software.inboundservice.dto.StockChangeRequest;
import org.software.inboundservice.dto.StockDTO;
import org.software.inboundservice.mapper.InboundRecordMapper;
import org.software.inboundservice.po.InboundRecord;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 入库业务逻辑 —— 微服务间协作链路示例（Feign 调用库存服务）
 * 入库流程：① 校验库存服务中存在该商品档案 ② 保存入库单（事实留痕） ③ 通知库存服务增加库存
 */
@Service
@AllArgsConstructor
@Slf4j
public class InboundService {

    private final InboundRecordMapper inboundRecordMapper;
    private final InventoryClient inventoryClient;

    /**
     * 执行入库
     */
    public Result inbound(InboundRequest request) {
        // ① 校验库存档案：库存目录中没有该商品则不允许入库
        StockDTO stock = inventoryClient.getStock(request.getProductId());
        if (stock == null) {
            return new Result(false, "库房无该商品档案，无法入库");
        }

        // ② 生成入库单号并保存入库单（RK + 时间 + 随机3位）
        InboundRecord record = new InboundRecord();
        record.setBillNo(genBillNo("RK"));
        record.setProductId(request.getProductId());
        record.setProductName(stock.getProductName());
        record.setQuantity(request.getQuantity());
        record.setOperator(request.getOperator());
        record.setInboundTime(new Date());
        inboundRecordMapper.insert(record);
        log.info("入库单已登记：{} 商品={} 数量={}", record.getBillNo(), record.getProductName(), record.getQuantity());

        // ③ Feign 调用库存服务增加库存
        Result increase = inventoryClient.increase(
                new StockChangeRequest(request.getProductId(), request.getQuantity()));
        if (!increase.isSuccess()) {
            return increase;
        }
        return new Result(true, "入库成功，单号=" + record.getBillNo());
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
