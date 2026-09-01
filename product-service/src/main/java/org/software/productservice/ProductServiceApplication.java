package org.software.productservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 商品服务（服务提供者）启动类 —— 指导书 2.4.3.3
 *
 * @SpringBootApplication 标识 Spring Boot 入口，自动扫描本包及子包下的组件
 * @MapperScan 告诉 Spring 去指定包寻找 MyBatis 的 Mapper 接口并注册为 Bean
 */
@SpringBootApplication
@MapperScan("org.software.productservice.mapper")
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}
