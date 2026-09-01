package org.software.statisticsservice.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.software.statisticsservice.po.DailySummary;

import java.util.List;

/**
 * 日报汇总数据访问接口（MyBatis 注解方式）
 * 使用 ON DUPLICATE KEY UPDATE 实现“插入/更新”二合一（表上有唯一键 stat_date + product_id）
 */
public interface DailySummaryMapper {

    /**
     * 写入某商品某日的汇总：已存在则覆盖（重复生成统计不会产生脏数据）
     */
    @Insert("insert into daily_summary(stat_date, product_id, product_name, in_qty, out_qty, stock_qty) " +
            "values(#{statDate}, #{productId}, #{productName}, #{inQty}, #{outQty}, #{stockQty}) " +
            "on duplicate key update product_name = #{productName}, in_qty = #{inQty}, " +
            "out_qty = #{outQty}, stock_qty = #{stockQty}")
    int insertOrUpdate(DailySummary summary);

    /**
     * 查询指定日期的日报
     */
    @Select("select id, stat_date as statDate, product_id as productId, product_name as productName, " +
            "in_qty as inQty, out_qty as outQty, stock_qty as stockQty " +
            "from daily_summary where stat_date = #{statDate} order by product_id")
    List<DailySummary> queryByDate(String statDate);
}
