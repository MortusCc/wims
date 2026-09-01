package org.software.productclient.client;

import lombok.extern.slf4j.Slf4j;
import org.software.productclient.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 熔断降级处理类 —— 指导书 2.6.1
 * 实现 ProductServiceClient 接口；当远程调用超时/失败触发 Hystrix 熔断时，
 * Feign 会自动调用本类中对应的方法作为兜底返回。
 * 这里按指导书仅返回 null（日志先打印 callback 便于观察），生产环境中应返回友好的兜底数据。
 *
 * 注意：必须加 @Component 交给 Spring 管理，且要能被启动类扫描到（见启动类 scanBasePackages）
 */
@Slf4j
@Component
public class ProductServiceFallback implements ProductServiceClient {

    /**
     * 按商品 id 查询商品的降级处理
     */
    @Override
    public Product findByProductId(Long productId) {
        log.info("findByProductId callback");
        return null;
    }

    /**
     * 查询所有商品的降级处理
     */
    @Override
    public List<Product> queryAllProduct() {
        log.info("queryAllProduct callback");
        return null;
    }
}
