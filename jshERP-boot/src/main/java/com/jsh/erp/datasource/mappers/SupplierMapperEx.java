package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.Supplier;
import com.jsh.erp.datasource.vo.CustomerAnalysisVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SupplierMapperEx {

    List<Supplier> selectByConditionSupplier(
            @Param("supplier") String supplier,
            @Param("type") String type,
            @Param("contacts") String contacts,
            @Param("phonenum") String phonenum,
            @Param("telephone") String telephone,
            @Param("creatorArray") String[] creatorArray);

    List<CustomerAnalysisVo> selectCustomerAnalysisList(
            @Param("customerName") String customerName,
            @Param("customerLevel") String customerLevel,
            @Param("customerSource") String customerSource,
            @Param("valueLevel") String valueLevel,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime,
            @Param("creatorArray") String[] creatorArray);

    Long countCustomerAnalysis(
            @Param("customerName") String customerName,
            @Param("customerLevel") String customerLevel,
            @Param("customerSource") String customerSource,
            @Param("valueLevel") String valueLevel,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime,
            @Param("creatorArray") String[] creatorArray);

    int batchDeleteSupplierByIds(@Param("updateTime") java.util.Date updateTime,
                                  @Param("updater") Long updater,
                                  @Param("ids") String[] ids);

    List<Supplier> findByAll(@Param("supplier") String supplier,
                              @Param("type") String type,
                              @Param("phonenum") String phonenum,
                              @Param("telephone") String telephone);

    Supplier getSupplierByNameAndType(@Param("supplier") String supplier,
                                        @Param("type") String type);

    List<com.jsh.erp.datasource.vo.SupplierSimple> getAllCustomer();

    List<Supplier> findByTypeAndKey(@Param("type") String type,
                                     @Param("key") String key,
                                     @Param("limit") Integer limit);

    Supplier getInfoById(@Param("id") Long id);

    Supplier getInfoByName(@Param("name") String name,
                            @Param("type") String type);
}
