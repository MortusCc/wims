package org.software.inventoryservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 库存服务（服务提供者）启动类 —— WIMS 库存管理微服务
 *
 * @SpringBootApplication 标识 Spring Boot 入口，自动扫描本包及子包下的组件
 * @MapperScan 告诉 Spring 去指定包寻找 MyBatis 的 Mapper 接口并注册为 Bean
 */
@SpringBootApplication
@MapperScan("org.software.inventoryservice.mapper")
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
}
