package com.jsh.erp.service;

import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.constants.BusinessConstants;
import com.jsh.erp.datasource.entities.CustomerFollowUp;
import com.jsh.erp.datasource.entities.Supplier;
import com.jsh.erp.datasource.entities.User;
import com.jsh.erp.datasource.mappers.CustomerFollowUpMapper;
import com.jsh.erp.datasource.mappers.CustomerFollowUpMapperEx;
import com.jsh.erp.datasource.mappers.SupplierMapper;
import com.jsh.erp.exception.JshException;
import com.jsh.erp.utils.PageUtils;
import com.jsh.erp.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CustomerFollowUpService {
    private Logger logger = LoggerFactory.getLogger(CustomerFollowUpService.class);

    @Resource
    private CustomerFollowUpMapper customerFollowUpMapper;

    @Resource
    private CustomerFollowUpMapperEx customerFollowUpMapperEx;

    @Resource
    private SupplierMapper supplierMapper;

    @Resource
    private LogService logService;

    @Resource
    private UserService userService;

    public CustomerFollowUp getCustomerFollowUp(long id) throws Exception {
        CustomerFollowUp result = null;
        try {
            result = customerFollowUpMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<CustomerFollowUp> getCustomerFollowUpListByCustomerId(Long customerId) throws Exception {
        List<CustomerFollowUp> list = new ArrayList<>();
        try {
            PageUtils.startPage();
            list = customerFollowUpMapperEx.selectByCondition(customerId, null, null, null, null, null, null, null);
        } catch (Exception e) {
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<CustomerFollowUp> select(String customerId, String followUpType, String followUpStatus,
                                          String operator, String beginTime, String endTime) throws Exception {
        List<CustomerFollowUp> list = new ArrayList<>();
        try {
            Long cId = StringUtil.isNotEmpty(customerId) ? Long.parseLong(customerId) : null;
            PageUtils.startPage();
            list = customerFollowUpMapperEx.selectByCondition(cId, followUpType, followUpStatus, operator,
                    beginTime, endTime, null, null);
        } catch (Exception e) {
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long countCustomerFollowUp(String customerId, String followUpType, String followUpStatus,
                                       String operator, String beginTime, String endTime) throws Exception {
        Long result = null;
        try {
            Long cId = StringUtil.isNotEmpty(customerId) ? Long.parseLong(customerId) : null;
            result = customerFollowUpMapperEx.countsByCondition(cId, followUpType, followUpStatus, operator,
                    beginTime, endTime);
        } catch (Exception e) {
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertCustomerFollowUp(String beanJson, HttpServletRequest request) throws Exception {
        CustomerFollowUp customerFollowUp = JSONObject.parseObject(beanJson, CustomerFollowUp.class);
        int result = 0;
        try {
            customerFollowUp.setCreateTime(new Date());
            customerFollowUp.setUpdateTime(new Date());
            customerFollowUp.setDeleteFlag(BusinessConstants.DELETE_FLAG_EXISTS);
            result = customerFollowUpMapper.insertSelective(customerFollowUp);
            logService.insertLog("客户跟进记录",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(customerFollowUp.getContent()).toString(),
                    request);
        } catch (Exception e) {
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateCustomerFollowUp(String beanJson, Long id, HttpServletRequest request) throws Exception {
        CustomerFollowUp customerFollowUp = JSONObject.parseObject(beanJson, CustomerFollowUp.class);
        customerFollowUp.setId(id);
        int result = 0;
        try {
            customerFollowUp.setUpdateTime(new Date());
            result = customerFollowUpMapper.updateByPrimaryKeySelective(customerFollowUp);
            logService.insertLog("客户跟进记录",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(id).toString(),
                    request);
        } catch (Exception e) {
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteCustomerFollowUp(Long id, HttpServletRequest request) throws Exception {
        int result = 0;
        try {
            CustomerFollowUp customerFollowUp = new CustomerFollowUp();
            customerFollowUp.setId(id);
            customerFollowUp.setDeleteFlag(BusinessConstants.DELETE_FLAG_DELETED);
            result = customerFollowUpMapper.updateByPrimaryKeySelective(customerFollowUp);
            logService.insertLog("客户跟进记录",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append(id).toString(),
                    request);
        } catch (Exception e) {
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteCustomerFollowUp(String ids, HttpServletRequest request) throws Exception {
        int result = 0;
        try {
            String[] idArray = ids.split(",");
            result = customerFollowUpMapperEx.batchDeleteByIds(idArray);
            logService.insertLog("客户跟进记录",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append("批量-").append(ids).toString(),
                    request);
        } catch (Exception e) {
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public int checkIsNameExist(Long id, String name) throws Exception {
        return 1;
    }

    public int batchSetStatus(String status, String ids, HttpServletRequest request) throws Exception {
        int result = 0;
        try {
            String[] idArray = ids.split(",");
            for (String idStr : idArray) {
                Long id = Long.parseLong(idStr);
                CustomerFollowUp customerFollowUp = new CustomerFollowUp();
                customerFollowUp.setId(id);
                customerFollowUp.setFollowUpStatus(status);
                customerFollowUp.setUpdateTime(new Date());
                result += customerFollowUpMapper.updateByPrimaryKeySelective(customerFollowUp);
            }
            logService.insertLog("客户跟进记录",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(ids).toString(),
                    request);
        } catch (Exception e) {
            JshException.writeFail(logger, e);
        }
        return result;
    }

    // ==================== 经手记录相关方法 ====================

    public List<CustomerFollowUp> selectHandlerRecord(String customerId, String handlerId,
                                                       String beginTime, String endTime) throws Exception {
        List<CustomerFollowUp> list = new ArrayList<>();
        try {
            Long cId = StringUtil.isNotEmpty(customerId) ? Long.parseLong(customerId) : null;
            Long hId = StringUtil.isNotEmpty(handlerId) ? Long.parseLong(handlerId) : null;
            PageUtils.startPage();
            list = customerFollowUpMapperEx.selectHandlerRecordByCondition(cId, hId, beginTime, endTime, null, null);
        } catch (Exception e) {
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long countHandlerRecord(String customerId, String handlerId,
                                    String beginTime, String endTime) throws Exception {
        Long result = null;
        try {
            Long cId = StringUtil.isNotEmpty(customerId) ? Long.parseLong(customerId) : null;
            Long hId = StringUtil.isNotEmpty(handlerId) ? Long.parseLong(handlerId) : null;
            result = customerFollowUpMapperEx.countsHandlerRecordByCondition(cId, hId, beginTime, endTime);
        } catch (Exception e) {
            JshException.readFail(logger, e);
        }
        return result;
    }

    public CustomerFollowUp getCurrentHandler(Long customerId) throws Exception {
        CustomerFollowUp result = null;
        try {
            result = customerFollowUpMapperEx.getCurrentHandler(customerId);
        } catch (Exception e) {
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertHandlerRecord(String beanJson, HttpServletRequest request) throws Exception {
        CustomerFollowUp handlerRecord = JSONObject.parseObject(beanJson, CustomerFollowUp.class);
        int result = 0;
        try {
            // 结束上一个经手记录
            CustomerFollowUp currentHandler = getCurrentHandler(handlerRecord.getCustomerId());
            if (currentHandler != null) {
                currentHandler.setNextFollowUpTime(new Date());
                currentHandler.setUpdateTime(new Date());
                customerFollowUpMapper.updateByPrimaryKeySelective(currentHandler);
            }

            // 设置新记录
            handlerRecord.setCreateTime(new Date());
            handlerRecord.setUpdateTime(new Date());
            handlerRecord.setDeleteFlag(BusinessConstants.DELETE_FLAG_EXISTS);
            handlerRecord.setIsHandlerRecord("1");
            handlerRecord.setFollowUpType("经手记录");

            // 获取经手人名称
            if (handlerRecord.getHandlerId() != null) {
                User handler = userService.getUser(handlerRecord.getHandlerId());
                if (handler != null) {
                    handlerRecord.setHandlerName(handler.getUsername());
                }
            }

            result = customerFollowUpMapper.insertSelective(handlerRecord);

            // 更新客户的当前经手人
            Supplier supplier = new Supplier();
            supplier.setId(handlerRecord.getCustomerId());
            supplier.setCurrentHandler(handlerRecord.getHandlerId());
            supplierMapper.updateByPrimaryKeySelective(supplier);

            logService.insertLog("客户经手记录",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(handlerRecord.getId()).toString(),
                    request);
        } catch (Exception e) {
            JshException.writeFail(logger, e);
        }
        return result;
    }
}
