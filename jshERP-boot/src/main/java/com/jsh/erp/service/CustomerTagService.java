package com.jsh.erp.service;

import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.constants.BusinessConstants;
import com.jsh.erp.datasource.entities.CustomerTag;
import com.jsh.erp.datasource.mappers.CustomerTagMapper;
import com.jsh.erp.exception.JshException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class CustomerTagService {
    private Logger logger = LoggerFactory.getLogger(CustomerTagService.class);

    @Resource
    private CustomerTagMapper customerTagMapper;

    @Resource
    private LogService logService;

    public CustomerTag getCustomerTag(long id) throws Exception {
        CustomerTag result = null;
        try {
            result = customerTagMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<CustomerTag> getAllCustomerTags(Long tenantId) throws Exception {
        List<CustomerTag> list = null;
        try {
            list = customerTagMapper.selectAll(tenantId);
        } catch (Exception e) {
            JshException.readFail(logger, e);
        }
        return list;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertCustomerTag(String beanJson, HttpServletRequest request) throws Exception {
        CustomerTag customerTag = JSONObject.parseObject(beanJson, CustomerTag.class);
        int result = 0;
        try {
            customerTag.setCreateTime(new Date());
            customerTag.setDeleteFlag(BusinessConstants.DELETE_FLAG_EXISTS);
            result = customerTagMapper.insertSelective(customerTag);
            logService.insertLog("客户标签",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(customerTag.getTagName()).toString(),
                    request);
        } catch (Exception e) {
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateCustomerTag(String beanJson, Long id, HttpServletRequest request) throws Exception {
        CustomerTag customerTag = JSONObject.parseObject(beanJson, CustomerTag.class);
        customerTag.setId(id);
        int result = 0;
        try {
            result = customerTagMapper.updateByPrimaryKeySelective(customerTag);
            logService.insertLog("客户标签",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(id).toString(),
                    request);
        } catch (Exception e) {
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteCustomerTag(Long id, HttpServletRequest request) throws Exception {
        int result = 0;
        try {
            CustomerTag customerTag = new CustomerTag();
            customerTag.setId(id);
            customerTag.setDeleteFlag(BusinessConstants.DELETE_FLAG_DELETED);
            result = customerTagMapper.updateByPrimaryKeySelective(customerTag);
            logService.insertLog("客户标签",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append(id).toString(),
                    request);
        } catch (Exception e) {
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteCustomerTag(String ids, HttpServletRequest request) throws Exception {
        int result = 0;
        try {
            String[] idArray = ids.split(",");
            result = customerTagMapper.batchDeleteByIds(idArray);
            logService.insertLog("客户标签",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append("批量-").append(ids).toString(),
                    request);
        } catch (Exception e) {
            JshException.writeFail(logger, e);
        }
        return result;
    }
}
