package org.software.alertservice.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.software.alertservice.po.AlertRecord;

import java.util.List;

/**
 * 库存预警数据访问接口（MyBatis 注解方式）
 */
public interface AlertRecordMapper {

    /**
     * 新增预警记录
     */
    @Insert("insert into alert_record(product_id, product_name, alert_type, cur_quantity, safe_stock, alert_time, status) " +
            "values(#{productId}, #{productName}, #{alertType}, #{curQuantity}, #{safeStock}, #{alertTime}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AlertRecord record);

    /**
     * 查询全部预警记录（未处理的排前面）
     */
    @Select("select id, product_id as productId, product_name as productName, alert_type as alertType, " +
            "cur_quantity as curQuantity, safe_stock as safeStock, alert_time as alertTime, status " +
            "from alert_record order by status asc, id desc")
    List<AlertRecord> queryAll();

    /**
     * 统计指定商品同类型未处理的预警数量（用于去重：已有未处理预警则不再重复生成）
     */
    @Select("select count(*) from alert_record where product_id = #{productId} and alert_type = #{alertType} and status = 0")
    int countUnhandled(@Param("productId") Long productId, @Param("alertType") String alertType);

    /**
     * 将预警标记为已处理
     */
    @Update("update alert_record set status = 1 where id = #{id}")
    int updateStatus(@Param("id") Long id);
}
