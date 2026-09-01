package org.software.inboundservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 入库服务启动类 —— WIMS 入库管理微服务
 * 通过 Feign 调用库存服务（inventory-service）完成“入库 → 库存增加”的业务链路
 *
 * @EnableFeignClients 扫描并注册本包下的 Feign 客户端接口（服务间调用）
 */
@SpringBootApplication
@MapperScan("org.software.inboundservice.mapper")
@EnableFeignClients("org.software.inboundservice.client")
public class InboundServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InboundServiceApplication.class, args);
    }
}
