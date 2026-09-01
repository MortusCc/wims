package org.software;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka 注册中心启动类
 *
 * @EnableEurekaServer 标识该应用是一个 Eureka 服务端（注册中心）。
 * 指导书 2.1.3.2 明确要求在主类上加此注解。
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServiceApplication.class, args);
    }
}
