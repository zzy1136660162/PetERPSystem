package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.CustomerFollowUp;
import com.jsh.erp.datasource.entities.CustomerFollowUpExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CustomerFollowUpMapper {
    long countByExample(CustomerFollowUpExample example);

    int deleteByExample(CustomerFollowUpExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CustomerFollowUp record);

    int insertSelective(CustomerFollowUp record);

    List<CustomerFollowUp> selectByExample(CustomerFollowUpExample example);

    CustomerFollowUp selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CustomerFollowUp record, @Param("example") CustomerFollowUpExample example);

    int updateByExample(@Param("record") CustomerFollowUp record, @Param("example") CustomerFollowUpExample example);

    int updateByPrimaryKeySelective(CustomerFollowUp record);

    int updateByPrimaryKey(CustomerFollowUp record);
}
