package org.software.alertservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.software.alertservice.mapper.AlertRecordMapper;
import org.software.alertservice.po.AlertRecord;
import org.software.alertservice.service.AlertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 预警服务控制层 —— WIMS 库存预警微服务
 * 对外提供：触发一次预警检查（GET）、查询预警记录（GET）、处理预警（GET）
 */
@AllArgsConstructor
@RestController
@Slf4j
public class AlertController {

    private final AlertService alertService;
    private final AlertRecordMapper alertRecordMapper;

    /**
     * 触发一次库存预警检查：拉全量库存比对安全阈值，不足则生成 LOW 预警（自动去重）
     */
    @GetMapping("/alert/check")
    public Map<String, Object> check() {
        Map<String, Object> result = alertService.checkStock();
        log.info("-------------OK   /alert/check--------------------");
        return result;
    }

    /**
     * 查询全部预警记录
     */
    @GetMapping("/alert/list")
    public List<AlertRecord> list() {
        List<AlertRecord> records = alertRecordMapper.queryAll();
        log.info("-------------OK   queryAllAlert--------------------");
        return records;
    }

    /**
     * 处理预警：将指定预警标记为已处理（如：完成补货后处理对应预警）
     */
    @GetMapping("/alert/deal/{id}")
    public String deal(@PathVariable("id") Long id) {
        int rows = alertRecordMapper.updateStatus(id);
        log.info("-------------OK   /alert/deal/{id}--------------------");
        return rows > 0 ? "预警已处理" : "预警不存在";
    }
}
