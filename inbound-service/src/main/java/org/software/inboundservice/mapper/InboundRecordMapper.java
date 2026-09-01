package org.software.inboundservice.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.software.inboundservice.po.InboundRecord;

import java.util.List;

/**
 * 入库单数据访问接口（MyBatis 注解方式）
 */
public interface InboundRecordMapper {

    /**
     * 新增入库单（useGeneratedKeys 回填自增主键）
     */
    @Insert("insert into inbound_record(bill_no, product_id, product_name, quantity, operator, inbound_time) " +
            "values(#{billNo}, #{productId}, #{productName}, #{quantity}, #{operator}, #{inboundTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(InboundRecord record);

    /**
     * 查询全部入库单
     */
    @Select("select id, bill_no as billNo, product_id as productId, product_name as productName, quantity, operator, inbound_time as inboundTime " +
            "from inbound_record order by id desc")
    List<InboundRecord> queryAll();

    /**
     * 按单号查询入库单
     */
    @Select("select id, bill_no as billNo, product_id as productId, product_name as productName, quantity, operator, inbound_time as inboundTime " +
            "from inbound_record where bill_no = #{billNo}")
    InboundRecord findByBillNo(String billNo);
}
