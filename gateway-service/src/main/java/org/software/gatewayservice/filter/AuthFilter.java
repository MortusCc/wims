package org.software.gatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * 统一鉴权过滤器（全局过滤器 GlobalFilter）—— 指导书 2.7.4.2
 * <p>
 * 全局过滤器作用于所有路由：请求参数中携带 token=1 才放行，否则直接返回 401 结束请求。
 * 这样鉴权逻辑统一收口在网关卡，无需每个微服务自己判断。
 * <p>
 * 注意：指导书原代码为 token.equals("1") —— 当请求没有 token 参数时 getFirst 返回 null，
 * 直接调用 equals 会抛空指针异常（返回 500 而不是 401）。这里先判空再比较，才能按预期返回 401。
 */
@Component
public class AuthFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求参数中的 token
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        System.out.println(token);
        // 修复指导书笔误：未携带 token（null）或 token 不等于 1 均返回 401
        if (token == null || !token.equals("1")) {
            // 响应 http 状态码（401，未认证）
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            // 请求结束，不再继续过滤器链
            return exchange.getResponse().setComplete();
        }
        // 继续执行过滤器链中的下一个资源（放行请求）
        return chain.filter(exchange);
    }
}
