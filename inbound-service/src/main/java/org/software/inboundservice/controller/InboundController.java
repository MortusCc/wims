package org.software.inboundservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.software.inboundservice.dto.InboundRequest;
import org.software.inboundservice.dto.Result;
import org.software.inboundservice.mapper.InboundRecordMapper;
import org.software.inboundservice.po.InboundRecord;
import org.software.inboundservice.service.InboundService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 入库服务控制层 —— WIMS 入库管理微服务
 * 对外提供：录入入库单（POST）、查询入库单（GET）
 */
@AllArgsConstructor
@RestController
@Slf4j
public class InboundController {

    private final InboundService inboundService;
    private final InboundRecordMapper inboundRecordMapper;

    /**
     * 录入一张入库单：校验商品档案 → 保存单据 → 通知库存服务增加库存
     * 演示请求体：{"productId":1,"quantity":100,"operator":"张伟"}
     */
    @PostMapping("/inbound")
    public Result inbound(@RequestBody InboundRequest request) {
        Result result = inboundService.inbound(request);
        log.info("-------------OK   /inbound  result={}--------------------", result.getMessage());
        return result;
    }

    /**
     * 查询全部入库单
     */
    @GetMapping("/inbound/list")
    public List<InboundRecord> list() {
        List<InboundRecord> records = inboundRecordMapper.queryAll();
        log.info("-------------OK   queryAllInbound--------------------");
        return records;
    }

    /**
     * 按单号查询入库单（入库回执）
     */
    @GetMapping("/inbound/{billNo}")
    public InboundRecord findByBillNo(@PathVariable("billNo") String billNo) {
        InboundRecord record = inboundRecordMapper.findByBillNo(billNo);
        log.info("-------------OK   /inbound/{billNo}--------------------");
        return record;
    }
}
