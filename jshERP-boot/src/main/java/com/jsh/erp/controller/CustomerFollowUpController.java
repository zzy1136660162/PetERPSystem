package com.jsh.erp.controller;

import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.base.BaseController;
import com.jsh.erp.base.TableDataInfo;
import com.jsh.erp.datasource.entities.CustomerFollowUp;
import com.jsh.erp.service.CustomerFollowUpService;
import com.jsh.erp.utils.Constants;
import com.jsh.erp.utils.ErpInfo;
import com.jsh.erp.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jsh.erp.utils.ResponseJsonUtil.returnJson;
import static com.jsh.erp.utils.ResponseJsonUtil.returnStr;

/**
 * 客户跟进记录管理
 */
@RestController
@RequestMapping(value = "/customerFollowUp")
@Api(tags = {"客户跟进记录"})
public class CustomerFollowUpController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(CustomerFollowUpController.class);

    @Resource
    private CustomerFollowUpService customerFollowUpService;

    @GetMapping(value = "/info")
    @ApiOperation(value = "根据id获取跟进记录")
    public String getInfo(@RequestParam("id") Long id,
                          HttpServletRequest request) throws Exception {
        CustomerFollowUp customerFollowUp = customerFollowUpService.getCustomerFollowUp(id);
        Map<String, Object> objectMap = new HashMap<>();
        if (customerFollowUp != null) {
            objectMap.put("info", customerFollowUp);
            return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
        } else {
            return returnJson(objectMap, ErpInfo.ERROR.name, ErpInfo.ERROR.code);
        }
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "获取跟进记录列表")
    public TableDataInfo getList(@RequestParam(value = Constants.SEARCH, required = false) String search,
                                 HttpServletRequest request) throws Exception {
        String customerId = StringUtil.getInfo(search, "customerId");
        String followUpType = StringUtil.getInfo(search, "followUpType");
        String followUpStatus = StringUtil.getInfo(search, "followUpStatus");
        String operator = StringUtil.getInfo(search, "operator");
        String beginTime = StringUtil.getInfo(search, "beginTime");
        String endTime = StringUtil.getInfo(search, "endTime");
        List<CustomerFollowUp> list = customerFollowUpService.select(customerId, followUpType, followUpStatus,
                operator, beginTime, endTime);
        return getDataTable(list);
    }

    @GetMapping(value = "/listByCustomer")
    @ApiOperation(value = "根据客户ID获取跟进记录")
    public TableDataInfo getListByCustomer(@RequestParam("customerId") Long customerId,
                                           HttpServletRequest request) throws Exception {
        List<CustomerFollowUp> list = customerFollowUpService.getCustomerFollowUpListByCustomerId(customerId);
        return getDataTable(list);
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "新增跟进记录")
    public String addResource(@RequestBody JSONObject obj, HttpServletRequest request) throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        int insert = customerFollowUpService.insertCustomerFollowUp(obj.toJSONString(), request);
        return returnStr(objectMap, insert);
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "修改跟进记录")
    public String updateResource(@RequestBody JSONObject obj, HttpServletRequest request) throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        Long id = obj.getLong("id");
        int update = customerFollowUpService.updateCustomerFollowUp(obj.toJSONString(), id, request);
        return returnStr(objectMap, update);
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "删除跟进记录")
    public String deleteResource(@RequestParam("id") Long id, HttpServletRequest request) throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        int delete = customerFollowUpService.deleteCustomerFollowUp(id, request);
        return returnStr(objectMap, delete);
    }

    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除跟进记录")
    public String batchDeleteResource(@RequestParam("ids") String ids, HttpServletRequest request) throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        int delete = customerFollowUpService.batchDeleteCustomerFollowUp(ids, request);
        return returnStr(objectMap, delete);
    }

    @PostMapping(value = "/batchSetStatus")
    @ApiOperation(value = "批量设置跟进状态")
    public String batchSetStatus(@RequestBody JSONObject obj, HttpServletRequest request) throws Exception {
        String status = obj.getString("status");
        String ids = obj.getString("ids");
        Map<String, Object> objectMap = new HashMap<>();
        int update = customerFollowUpService.batchSetStatus(status, ids, request);
        return returnStr(objectMap, update);
    }

    // ==================== 经手记录相关API ====================

    @GetMapping(value = "/handlerRecordList")
    @ApiOperation(value = "获取经手记录列表")
    public TableDataInfo getHandlerRecordList(@RequestParam(value = Constants.SEARCH, required = false) String search,
                                               HttpServletRequest request) throws Exception {
        String customerId = StringUtil.getInfo(search, "customerId");
        String handlerId = StringUtil.getInfo(search, "handlerId");
        String beginTime = StringUtil.getInfo(search, "beginTime");
        String endTime = StringUtil.getInfo(search, "endTime");
        List<CustomerFollowUp> list = customerFollowUpService.selectHandlerRecord(customerId, handlerId, beginTime, endTime);
        return getDataTable(list);
    }

    @GetMapping(value = "/getCurrentHandler")
    @ApiOperation(value = "获取客户当前经手人")
    public String getCurrentHandler(@RequestParam("customerId") Long customerId,
                                     HttpServletRequest request) throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        CustomerFollowUp handler = customerFollowUpService.getCurrentHandler(customerId);
        if (handler != null) {
            objectMap.put("info", handler);
            return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
        } else {
            return returnJson(objectMap, ErpInfo.ERROR.name, ErpInfo.ERROR.code);
        }
    }

    @PostMapping(value = "/addHandlerRecord")
    @ApiOperation(value = "新增经手记录")
    public String addHandlerRecord(@RequestBody JSONObject obj, HttpServletRequest request) throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        int insert = customerFollowUpService.insertHandlerRecord(obj.toJSONString(), request);
        return returnStr(objectMap, insert);
    }
}
