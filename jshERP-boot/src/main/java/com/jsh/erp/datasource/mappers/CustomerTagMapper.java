package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.CustomerTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerTagMapper {

    int deleteByPrimaryKey(Long id);

    int insert(CustomerTag record);

    int insertSelective(CustomerTag record);

    CustomerTag selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerTag record);

    int updateByPrimaryKey(CustomerTag record);

    List<CustomerTag> selectAll(@Param("tenantId") Long tenantId);

    int batchDeleteByIds(@Param("ids") String[] ids);
}
