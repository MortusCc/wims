package org.software.statisticsservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.software.statisticsservice.mapper.DailySummaryMapper;
import org.software.statisticsservice.po.DailySummary;
import org.software.statisticsservice.service.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 统计服务控制层 —— WIMS 库存统计微服务
 * 对外提供：生成当日日报（GET，Feign 聚合入库/出库/库存服务）、查询指定日期日报（GET）
 */
@AllArgsConstructor
@RestController
@Slf4j
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final DailySummaryMapper dailySummaryMapper;

    /**
     * 生成当日日报：Feign 聚合 入库 + 出库 + 库存 三个服务的数据 → 写入日报表并返回
     */
    @GetMapping("/statistics/daily")
    public List<DailySummary> daily() {
        List<DailySummary> list = statisticsService.dailyReport();
        log.info("-------------OK   /statistics/daily--------------------");
        return list;
    }

    /**
     * 查询指定日期的日报（不重新统计，只读库）
     */
    @GetMapping("/statistics/show/{statDate}")
    public List<DailySummary> show(@PathVariable("statDate") String statDate) {
        log.info("-------------OK   /statistics/show/{statDate}--------------------");
        return dailySummaryMapper.queryByDate(statDate);
    }

    /**
     * 查询今日日报（只读库）
     */
    @GetMapping("/statistics/showToday")
    public List<DailySummary> showToday() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        log.info("-------------OK   /statistics/showToday--------------------");
        return dailySummaryMapper.queryByDate(today);
    }
}
