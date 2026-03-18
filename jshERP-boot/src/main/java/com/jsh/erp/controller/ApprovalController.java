package com.jsh.erp.controller;

import com.jsh.erp.base.BaseController;
import com.jsh.erp.service.ApprovalService;
import com.jsh.erp.utils.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/approval")
@Api(tags = {"审批流程管理"})
public class ApprovalController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(ApprovalController.class);

    @Resource
    private ApprovalService approvalService;

    @PostMapping("/submit")
    @ApiOperation(value = "提交单据审批")
    public AjaxResult submitForApproval(@RequestParam("depotHeadId") Long depotHeadId,
                                       HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            return approvalService.submitForApproval(depotHeadId, userId);
        } catch (Exception e) {
            logger.error("提交审批失败", e);
            return AjaxResult.error("提交审批失败：" + e.getMessage());
        }
    }

    @PostMapping("/approve")
    @ApiOperation(value = "审批通过")
    public AjaxResult approve(@RequestParam("depotHeadId") Long depotHeadId,
                             @RequestParam(value = "comment", required = false) String comment,
                             HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            return approvalService.approveDepotHead(depotHeadId, userId, comment, "APPROVE");
        } catch (Exception e) {
            logger.error("审批失败", e);
            return AjaxResult.error("审批失败：" + e.getMessage());
        }
    }

    @PostMapping("/reject")
    @ApiOperation(value = "驳回单据")
    public AjaxResult reject(@RequestParam("depotHeadId") Long depotHeadId,
                            @RequestParam(value = "comment", required = false) String comment,
                            HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId(request);
            return approvalService.approveDepotHead(depotHeadId, userId, comment, "REJECT");
        } catch (Exception e) {
            logger.error("驳回失败", e);
            return AjaxResult.error("驳回失败：" + e.getMessage());
        }
    }

    @GetMapping("/status/{depotHeadId}")
    @ApiOperation(value = "获取审批状态")
    public AjaxResult getApprovalStatus(@PathVariable("depotHeadId") Long depotHeadId) {
        try {
            return approvalService.getApprovalStatus(depotHeadId);
        } catch (Exception e) {
            logger.error("获取审批状态失败", e);
            return AjaxResult.error("获取审批状态失败：" + e.getMessage());
        }
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        // 这里需要根据实际情况获取当前登录用户ID
        // 可以集成现有的用户认证系统
        Object userId = request.getSession().getAttribute("userId");
        return userId != null ? Long.valueOf(userId.toString()) : null;
    }
}
