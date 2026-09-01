package org.software.outboundservice.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.software.outboundservice.po.OutboundRecord;

import java.util.List;

/**
 * 出库单数据访问接口（MyBatis 注解方式）
 */
public interface OutboundRecordMapper {

    /**
     * 新增出库单（useGeneratedKeys 回填自增主键）
     */
    @Insert("insert into outbound_record(bill_no, product_id, product_name, quantity, operator, outbound_time) " +
            "values(#{billNo}, #{productId}, #{productName}, #{quantity}, #{operator}, #{outboundTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OutboundRecord record);

    /**
     * 查询全部出库单
     */
    @Select("select id, bill_no as billNo, product_id as productId, product_name as productName, quantity, operator, outbound_time as outboundTime " +
            "from outbound_record order by id desc")
    List<OutboundRecord> queryAll();

    /**
     * 按单号查询出库单
     */
    @Select("select id, bill_no as billNo, product_id as productId, product_name as productName, quantity, operator, outbound_time as outboundTime " +
            "from outbound_record where bill_no = #{billNo}")
    OutboundRecord findByBillNo(String billNo);
}
