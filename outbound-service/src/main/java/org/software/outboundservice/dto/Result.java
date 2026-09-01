package org.software.outboundservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库存服务返回结果（Feign 反序列化用）—— 与 inventory-service 中的 Result 字段一致
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    /** 是否成功 */
    private boolean success;
    /** 提示消息 */
    private String message;
}
