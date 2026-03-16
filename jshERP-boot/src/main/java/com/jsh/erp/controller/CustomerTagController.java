package com.jsh.erp.controller;

import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.base.BaseController;
import com.jsh.erp.base.TableDataInfo;
import com.jsh.erp.datasource.entities.CustomerTag;
import com.jsh.erp.service.CustomerTagService;
import com.jsh.erp.utils.ErpInfo;
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
 * 客户标签管理
 */
@RestController
@RequestMapping(value = "/customerTag")
@Api(tags = {"客户标签"})
public class CustomerTagController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(CustomerTagController.class);

    @Resource
    private CustomerTagService customerTagService;

    @GetMapping(value = "/info")
    @ApiOperation(value = "根据id获取标签")
    public String getInfo(@RequestParam("id") Long id,
                          HttpServletRequest request) throws Exception {
        CustomerTag customerTag = customerTagService.getCustomerTag(id);
        Map<String, Object> objectMap = new HashMap<>();
        if (customerTag != null) {
            objectMap.put("info", customerTag);
            return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
        } else {
            return returnJson(objectMap, ErpInfo.ERROR.name, ErpInfo.ERROR.code);
        }
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "获取标签列表")
    public TableDataInfo getList(HttpServletRequest request) throws Exception {
        List<CustomerTag> list = customerTagService.getAllCustomerTags(null);
        return getDataTable(list);
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "新增标签")
    public String addResource(@RequestBody JSONObject obj, HttpServletRequest request) throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        int insert = customerTagService.insertCustomerTag(obj.toJSONString(), request);
        return returnStr(objectMap, insert);
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "修改标签")
    public String updateResource(@RequestBody JSONObject obj, HttpServletRequest request) throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        Long id = obj.getLong("id");
        int update = customerTagService.updateCustomerTag(obj.toJSONString(), id, request);
        return returnStr(objectMap, update);
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "删除标签")
    public String deleteResource(@RequestParam("id") Long id, HttpServletRequest request) throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        int delete = customerTagService.deleteCustomerTag(id, request);
        return returnStr(objectMap, delete);
    }

    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除标签")
    public String batchDeleteResource(@RequestParam("ids") String ids, HttpServletRequest request) throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        int delete = customerTagService.batchDeleteCustomerTag(ids, request);
        return returnStr(objectMap, delete);
    }
}
