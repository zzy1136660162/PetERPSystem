package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.CustomerFollowUp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerFollowUpMapperEx {

    List<CustomerFollowUp> selectByCondition(
            @Param("customerId") Long customerId,
            @Param("followUpType") String followUpType,
            @Param("followUpStatus") String followUpStatus,
            @Param("operator") String operator,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime,
            @Param("offset") Integer offset,
            @Param("rows") Integer rows);

    Long countsByCondition(
            @Param("customerId") Long customerId,
            @Param("followUpType") String followUpType,
            @Param("followUpStatus") String followUpStatus,
            @Param("operator") String operator,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime);

    int batchDeleteByIds(@Param("ids") String ids[]);

    // 经手记录相关方法
    List<CustomerFollowUp> selectHandlerRecordByCondition(
            @Param("customerId") Long customerId,
            @Param("handlerId") Long handlerId,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime,
            @Param("offset") Integer offset,
            @Param("rows") Integer rows);

    Long countsHandlerRecordByCondition(
            @Param("customerId") Long customerId,
            @Param("handlerId") Long handlerId,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime);

    CustomerFollowUp getCurrentHandler(@Param("customerId") Long customerId);
}
