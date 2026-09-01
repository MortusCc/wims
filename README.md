# 仓库库存管理系统（WIMS）微服务

基于《云原生技术实践指导书（containerd版）》第 2 章的微服务系统课程项目。
按指导书「微服务一库一域」原则设计，包含 6 个业务域微服务 + Eureka 注册中心 + Gateway 网关。

技术栈：Spring Boot 2.0.9.RELEASE + Spring Cloud Finchley.SR2 + JDK 1.8 + MyBatis + MySQL 8.0

## 模块结构

| 模块 | 端口 | 职责 | 独立数据库 |
|---|---|---|---|
| eureka-service | 8888 | 服务注册中心 | - |
| gateway-service | 9999 | API 网关（路由 + token=1 鉴权） | - |
| product-service | 8010 | 商品管理（商品档案） | wims_product |
| product-client | 8018 | Feign 客户端（负载均衡/熔断演示） | - |
| inventory-service | 8020 | 库存台账（查询/增减，供其他服务 Feign 调用） | wims_inventory |
| inbound-service | 8021 | 入库（Feign → 库存服务增加库存） | wims_inbound |
| outbound-service | 8022 | 出库（Feign → 库存服务扣减，库存不足拦截） | wims_outbound |
| alert-service | 8023 | 库存预警（比对安全库存，生成 LOW 预警，自动去重） | wims_alert |
| statistics-service | 8024 | 库存统计（Feign 聚合出入库+库存 → 日报） | wims_statistics |

## 快速开始

1. 环境：JDK 1.8、Maven 3.x、MySQL 8.0（本地 root/root，127.0.0.1:3306）
2. 初始化数据库：执行 `sql/wims.sql`（幂等，可重复执行）
   ```bash
   mysql -uroot -proot < sql/wims.sql
   ```
3. 启动顺序：eureka-service → product-service（可起 8011 第二实例演示负载均衡）→ product-client → inventory-service → inbound-service → outbound-service → alert-service → statistics-service → gateway-service
   - 各模块启动命令：`mvn -pl <模块名> spring-boot:run`
   - VSCode 用户可直接用 `.vscode/tasks.json` 里的任务启动（已配置 512M 堆限制，节省内存）

## 业务验证示例（Powershell）

```powershell
# 商品
Invoke-RestMethod http://localhost:8018/findByProductId/1
Invoke-RestMethod http://localhost:9999/product/queryAllProduct?token=1   # 网关+鉴权

# 入库（Feign 调库存服务）
Invoke-RestMethod -Method Post -Uri http://localhost:8021/inbound -ContentType "application/json" -Body '{"productId":1,"quantity":100,"operator":"张伟"}'

# 出库（库存不足被拦截：抽纸当前库存50，出200 → 失败）
Invoke-RestMethod -Method Post -Uri http://localhost:8022/outbound -ContentType "application/json" -Body '{"productId":2,"quantity":200,"operator":"王强"}'

# 预警检查（低于安全库存 → 生成/去重）
Invoke-RestMethod http://localhost:8023/alert/check

# 日报统计（Feign 聚合出入库 + 库存）
Invoke-RestMethod http://localhost:8024/statistics/daily
```

## 示例业务数据

矿泉水 / 抽纸 / 大米 / 食用油 / 洗衣液 5 个商品；
其中 抽纸（50/安全线200）与 洗衣液（20/安全线50）初始库存低于安全阈值，用于预警演示。
