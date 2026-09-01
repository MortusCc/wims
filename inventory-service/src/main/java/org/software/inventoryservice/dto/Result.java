package org.software.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库存台账服务统一返回结果：成功标志 + 提示消息
 * 供入库/出库服务 Feign 调用时判断操作结果（如扣减失败返回 success=false, message=库存不足）
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
