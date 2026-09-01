package org.software.statisticsservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 库存统计服务启动类 —— WIMS 库存统计微服务
 * 通过 Feign 聚合 入库服务 + 出库服务 + 库存服务 的数据，生成按日汇总报表
 *
 * @EnableFeignClients 扫描并注册本包下的 Feign 客户端接口（服务间调用）
 */
@SpringBootApplication
@MapperScan("org.software.statisticsservice.mapper")
@EnableFeignClients("org.software.statisticsservice.client")
public class StatisticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatisticsServiceApplication.class, args);
    }
}
