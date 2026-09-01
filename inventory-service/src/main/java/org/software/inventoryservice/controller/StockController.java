package org.software.inventoryservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.software.inventoryservice.dto.Result;
import org.software.inventoryservice.dto.StockChangeRequest;
import org.software.inventoryservice.mapper.StockMapper;
import org.software.inventoryservice.po.Stock;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存服务控制层 —— WIMS 库存查询微服务
 * 对外提供库存查询与增减接口：入库/出库服务通过 Feign 调用本服务的增减接口；
 * 预警服务拉取全量库存比对安全阈值；统计服务聚合报表数据
 */
@AllArgsConstructor
@RestController
@Slf4j
public class StockController {

    private final StockMapper stockMapper;

    /**
     * 查询单个商品的库存
     */
    @GetMapping("/stock/{productId}")
    public Stock getStock(@PathVariable Long productId) {
        Stock stock = stockMapper.findByProductId(productId);
        log.info("-------------OK   /stock/{productId}--------------------");
        return stock;
    }

    /**
     * 查询全部库存
     */
    @GetMapping("/stock/list")
    public List<Stock> listStock() {
        List<Stock> stockList = stockMapper.queryAll();
        log.info("-------------OK   queryAllStock--------------------");
        return stockList;
    }

    /**
     * 增加库存（入库服务调用）：数量直接累加
     */
    @PostMapping("/stock/increase")
    public Result increase(@RequestBody StockChangeRequest request) {
        int rows = stockMapper.increaseQuantity(request.getProductId(), request.getQuantity());
        if (rows <= 0) {
            log.warn("库房无此商品档案，无法入库：productId={}", request.getProductId());
            return new Result(false, "库房无该商品档案，无法入库");
        }
        log.info("-------------OK   /stock/increase  productId={} +{}--------------------",
                request.getProductId(), request.getQuantity());
        return new Result(true, "库存增加成功");
    }

    /**
     * 扣减库存（出库服务调用）：where 限定了 quantity >= 数量，库存不足时影响行数为 0
     */
    @PostMapping("/stock/decrease")
    public Result decrease(@RequestBody StockChangeRequest request) {
        Stock stock = stockMapper.findByProductId(request.getProductId());
        if (stock == null) {
            return new Result(false, "库房无该商品档案，无法出库");
        }
        int rows = stockMapper.decreaseQuantity(request.getProductId(), request.getQuantity());
        if (rows <= 0) {
            log.warn("库存不足：productId={} 当前库存={} 需要={}", request.getProductId(),
                    stock.getQuantity(), request.getQuantity());
            return new Result(false, "库存不足，当前库存=" + stock.getQuantity());
        }
        log.info("-------------OK   /stock/decrease  productId={} -{}--------------------",
                request.getProductId(), request.getQuantity());
        return new Result(true, "库存扣减成功");
    }
}
