package org.software.outboundservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.software.outboundservice.dto.OutboundRequest;
import org.software.outboundservice.dto.Result;
import org.software.outboundservice.mapper.OutboundRecordMapper;
import org.software.outboundservice.po.OutboundRecord;
import org.software.outboundservice.service.OutboundService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 出库服务控制层 —— WIMS 出库管理微服务
 * 对外提供：录入出库单（POST，含库存不足拦截）、查询出库单（GET）
 */
@AllArgsConstructor
@RestController
@Slf4j
public class OutboundController {

    private final OutboundService outboundService;
    private final OutboundRecordMapper outboundRecordMapper;

    /**
     * 录入一张出库单：校验商品档案 → 扣减库存（不足拦截） → 保存出库单
     * 演示请求体：{"productId":3,"quantity":50,"operator":"王强"}（出库大米50，成功）
     * 演示请求体：{"productId":2,"quantity":200,"operator":"王强"}（出库抽纸200 > 库存50，被拦截）
     */
    @PostMapping("/outbound")
    public Result outbound(@RequestBody OutboundRequest request) {
        Result result = outboundService.outbound(request);
        log.info("-------------OK   /outbound  result={}--------------------", result.getMessage());
        return result;
    }

    /**
     * 查询全部出库单
     */
    @GetMapping("/outbound/list")
    public List<OutboundRecord> list() {
        List<OutboundRecord> records = outboundRecordMapper.queryAll();
        log.info("-------------OK   queryAllOutbound--------------------");
        return records;
    }

    /**
     * 按单号查询出库单（出库回执）
     */
    @GetMapping("/outbound/{billNo}")
    public OutboundRecord findByBillNo(@PathVariable("billNo") String billNo) {
        OutboundRecord record = outboundRecordMapper.findByBillNo(billNo);
        log.info("-------------OK   /outbound/{billNo}--------------------");
        return record;
    }
}
