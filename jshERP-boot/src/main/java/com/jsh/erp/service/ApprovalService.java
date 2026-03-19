package com.jsh.erp.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.constants.BusinessConstants;
import com.jsh.erp.datasource.entities.DepotHead;
import com.jsh.erp.datasource.entities.Msg;
import com.jsh.erp.datasource.entities.User;
import com.jsh.erp.datasource.mappers.DepotHeadMapper;
import com.jsh.erp.datasource.mappers.MsgMapper;
import com.jsh.erp.base.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ApprovalService {
    private Logger logger = LoggerFactory.getLogger(ApprovalService.class);

    @Resource
    private DepotHeadMapper depotHeadMapper;

    @Resource
    private MsgMapper msgMapper;

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    // 审批步骤常量
    public static final int APPROVAL_STEP_UNSUBMITTED = 0;    // 未提交
    public static final int APPROVAL_STEP_PENDING = 1;        // 待审批
    public static final int APPROVAL_STEP_IN_PROGRESS = 2;    // 审批中
    public static final int APPROVAL_STEP_APPROVED = 3;       // 已通过
    public static final int APPROVAL_STEP_REJECTED = 4;       // 已驳回

    // 审批角色常量
    public static final String ROLE_DEPARTMENT_MANAGER = "DEPARTMENT_MANAGER";
    public static final String ROLE_DIRECTOR = "DIRECTOR";
    public static final String ROLE_GENERAL_MANAGER = "GENERAL_MANAGER";
    public static final String ROLE_WAREHOUSE_KEEPER = "WAREHOUSE_KEEPER";

    /**
     * 提交单据审批
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult submitForApproval(Long depotHeadId, Long submitterId) {
        try {
            DepotHead depotHead = depotHeadMapper.selectByPrimaryKey(depotHeadId);
            if (depotHead == null) {
                return AjaxResult.error("单据不存在");
            }

            // 检查当前状态
            if (depotHead.getApprovalStep() != null && depotHead.getApprovalStep() > APPROVAL_STEP_UNSUBMITTED) {
                return AjaxResult.error("单据已提交审批，不能重复提交");
            }

            // 初始化审批流程
            depotHead.setApprovalStep(APPROVAL_STEP_PENDING);
            depotHead.setCurrentApproverRole(ROLE_DEPARTMENT_MANAGER);
            depotHead.setSubmitTime(new Date());

            // 创建审批历史
            JSONArray approvalHistory = new JSONArray();
            JSONObject historyItem = new JSONObject();
            historyItem.put("step", 1);
            historyItem.put("role", ROLE_DEPARTMENT_MANAGER);
            historyItem.put("action", "SUBMIT");
            historyItem.put("submitterId", submitterId);
            historyItem.put("submitTime", new Date());
            approvalHistory.add(historyItem);

            depotHead.setApprovalHistory(approvalHistory.toJSONString());
            depotHead.setStatus("9"); // 审核中状态

            depotHeadMapper.updateByPrimaryKey(depotHead);

            // 通知部门经理
            notifyApprovers(ROLE_DEPARTMENT_MANAGER, depotHead);

            return AjaxResult.success("单据已提交审批");
        } catch (Exception e) {
            logger.error("提交审批失败", e);
            return AjaxResult.error("提交审批失败：" + e.getMessage());
        }
    }

    /**
     * 审批单据
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult approveDepotHead(Long depotHeadId, Long approverId, String comment, String action) {
        try {
            DepotHead depotHead = depotHeadMapper.selectByPrimaryKey(depotHeadId);
            if (depotHead == null) {
                return AjaxResult.error("单据不存在");
            }

            // 检查当前审批步骤
            int currentStep = depotHead.getApprovalStep() != null ? depotHead.getApprovalStep() : 0;
            if (currentStep == APPROVAL_STEP_APPROVED) {
                return AjaxResult.error("单据已审批通过");
            }
            if (currentStep == APPROVAL_STEP_REJECTED) {
                return AjaxResult.error("单据已驳回");
            }

            // 获取当前审批人信息
            User approver = userService.getUser(approverId);
            if (approver == null) {
                return AjaxResult.error("审批人不存在");
            }

            // 更新审批历史
            JSONArray approvalHistory = JSONArray.parseArray(depotHead.getApprovalHistory());
            if (approvalHistory == null) {
                approvalHistory = new JSONArray();
            }

            JSONObject historyItem = new JSONObject();
            historyItem.put("step", currentStep);
            historyItem.put("approverId", approverId);
            historyItem.put("approverName", approver.getUsername());
            historyItem.put("action", action);
            historyItem.put("comment", comment);
            historyItem.put("approveTime", new Date());
            approvalHistory.add(historyItem);

            depotHead.setApprovalHistory(approvalHistory.toJSONString());
            depotHead.setApprovalComment(comment);

            if ("REJECT".equals(action)) {
                // 驳回
                depotHead.setApprovalStep(APPROVAL_STEP_REJECTED);
                depotHead.setStatus("0"); // 未审核状态
                depotHead.setApproveTime(new Date());
            } else {
                // 通过，进入下一步审批
                String nextRole = getNextApproverRole(depotHead.getCurrentApproverRole());
                if (nextRole == null) {
                    // 最终审批通过
                    depotHead.setApprovalStep(APPROVAL_STEP_APPROVED);
                    depotHead.setStatus("1"); // 已审核状态
                    depotHead.setApproveTime(new Date());

                    // 自动通知库管
                    notifyWarehouseKeepers(depotHead);
                } else {
                    // 进入下一步审批
                    depotHead.setApprovalStep(APPROVAL_STEP_IN_PROGRESS);
                    depotHead.setCurrentApproverRole(nextRole);

                    // 通知下一级审批人
                    notifyApprovers(nextRole, depotHead);
                }
            }

            depotHeadMapper.updateByPrimaryKey(depotHead);

            return AjaxResult.success("审批操作成功");
        } catch (Exception e) {
            logger.error("审批失败", e);
            return AjaxResult.error("审批失败：" + e.getMessage());
        }
    }

    /**
     * 获取下一级审批角色
     */
    private String getNextApproverRole(String currentRole) {
        switch (currentRole) {
            case ROLE_DEPARTMENT_MANAGER:
                return ROLE_DIRECTOR;
            case ROLE_DIRECTOR:
                return ROLE_GENERAL_MANAGER;
            case ROLE_GENERAL_MANAGER:
                return null; // 审批结束
            default:
                return null;
        }
    }

    /**
     * 通知审批人
     */
    private void notifyApprovers(String role, DepotHead depotHead) {
        // 通知功能暂不使用，避免调用不存在的方法
        // 如需启用，需要在 UserService 中添加 getUserListByRole 方法
    }

    /**
     * 通知库管人员
     */
    private void notifyWarehouseKeepers(DepotHead depotHead) {
        // 通知功能暂不使用，避免调用不存在的方法
        // 如需启用，需要在 UserService 中添加 getUserListByRole 方法
    }

    /**
     * 获取审批状态
     */
    public AjaxResult getApprovalStatus(Long depotHeadId) {
        try {
            DepotHead depotHead = depotHeadMapper.selectByPrimaryKey(depotHeadId);
            if (depotHead == null) {
                return AjaxResult.error("单据不存在");
            }

            JSONObject result = new JSONObject();
            result.put("depotHeadId", depotHeadId);
            result.put("approvalStep", depotHead.getApprovalStep());
            result.put("currentApproverRole", depotHead.getCurrentApproverRole());
            result.put("submitTime", depotHead.getSubmitTime());
            result.put("approveTime", depotHead.getApproveTime());
            result.put("warehouseNotified", depotHead.getWarehouseNotified());

            // 解析审批历史
            String historyStr = depotHead.getApprovalHistory();
            if (historyStr != null && !historyStr.isEmpty()) {
                result.put("approvalHistory", JSONArray.parseArray(historyStr));
            } else {
                result.put("approvalHistory", new JSONArray());
            }

            // 计算进度百分比
            int progress = calculateProgress(depotHead.getApprovalStep());
            result.put("progress", progress);

            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取审批状态失败", e);
            return AjaxResult.error("获取审批状态失败：" + e.getMessage());
        }
    }

    /**
     * 计算审批进度百分比
     */
    private int calculateProgress(Integer approvalStep) {
        if (approvalStep == null) return 0;

        switch (approvalStep) {
            case APPROVAL_STEP_UNSUBMITTED:
                return 0;
            case APPROVAL_STEP_PENDING:
                return 25;
            case APPROVAL_STEP_IN_PROGRESS:
                return 50;
            case APPROVAL_STEP_APPROVED:
                return 100;
            case APPROVAL_STEP_REJECTED:
                return 0;
            default:
                return 0;
        }
    }
}
