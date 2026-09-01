package org.software.outboundservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 出库服务启动类 —— WIMS 出库管理微服务
 * 通过 Feign 调用库存服务（inventory-service）完成“出库 → 库存扣减（含不足校验）”业务链路
 *
 * @EnableFeignClients 扫描并注册本包下的 Feign 客户端接口（服务间调用）
 */
@SpringBootApplication
@MapperScan("org.software.outboundservice.mapper")
@EnableFeignClients("org.software.outboundservice.client")
public class OutboundServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OutboundServiceApplication.class, args);
    }
}
