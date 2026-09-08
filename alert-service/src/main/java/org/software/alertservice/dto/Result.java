package org.software.alertservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 预警服务统一返回结果：成功标志 + 提示消息
 * 与库存/入库/出库服务的 Result 结构一致，便于前端与 Feign 按 success/message 统一处理
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
