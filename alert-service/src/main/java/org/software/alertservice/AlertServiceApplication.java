package org.software.alertservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 库存预警服务启动类 —— WIMS 库存预警微服务
 * 通过 Feign 拉取库存服务全量库存，与安全阈值对比，生成预警记录并支持处理
 *
 * @EnableFeignClients 扫描并注册本包下的 Feign 客户端接口（服务间调用）
 */
@SpringBootApplication
@MapperScan("org.software.alertservice.mapper")
@EnableFeignClients("org.software.alertservice.client")
public class AlertServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlertServiceApplication.class, args);
    }
}
