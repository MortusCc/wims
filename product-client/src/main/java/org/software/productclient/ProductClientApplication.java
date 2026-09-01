package org.software.productclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 商品服务客户端（服务消费者）启动类 —— 指导书 2.4.4.2 / 2.6.1
 *
 * @SpringBootApplication  标识 Spring Boot 入口，自动扫描本包及子包下的组件；
 *                         scanBasePackages（指导书 2.6 要求）：显式声明 controller 与 client 包，
 *                         确保放在 client 包下的 ProductServiceFallback 降级类能被扫描到
 * @EnableEurekaClient     注册到 Eureka，并能发现其他服务
 * @EnableFeignClients     启用 Feign 客户端，扫描 client 包下的 @FeignClient 接口
 */
@SpringBootApplication(scanBasePackages = {
        "org.software.productclient.controller",
        "org.software.productclient.client"})
@EnableEurekaClient
@EnableFeignClients("org.software.productclient.client")
public class ProductClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductClientApplication.class, args);
    }
}
