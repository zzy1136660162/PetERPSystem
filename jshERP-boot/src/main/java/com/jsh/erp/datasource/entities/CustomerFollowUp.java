package com.jsh.erp.datasource.entities;

import java.util.Date;

public class CustomerFollowUp {
    private Long id;

    private Long customerId;

    private Date followUpTime;

    private String followUpType;

    private String content;

    private Date nextFollowUpTime;

    private String followUpStatus;

    private String operator;

    private String remark;

    private Long tenantId;

    private String deleteFlag;

    private Date createTime;

    private Date updateTime;

    private Long handlerId;

    private String handlerName;

    private String isHandlerRecord;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Date getFollowUpTime() {
        return followUpTime;
    }

    public void setFollowUpTime(Date followUpTime) {
        this.followUpTime = followUpTime;
    }

    public String getFollowUpType() {
        return followUpType;
    }

    public void setFollowUpType(String followUpType) {
        this.followUpType = followUpType == null ? null : followUpType.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getNextFollowUpTime() {
        return nextFollowUpTime;
    }

    public void setNextFollowUpTime(Date nextFollowUpTime) {
        this.nextFollowUpTime = nextFollowUpTime;
    }

    public String getFollowUpStatus() {
        return followUpStatus;
    }

    public void setFollowUpStatus(String followUpStatus) {
        this.followUpStatus = followUpStatus == null ? null : followUpStatus.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag == null ? null : deleteFlag.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(Long handlerId) {
        this.handlerId = handlerId;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName == null ? null : handlerName.trim();
    }

    public String getIsHandlerRecord() {
        return isHandlerRecord;
    }

    public void setIsHandlerRecord(String isHandlerRecord) {
        this.isHandlerRecord = isHandlerRecord == null ? null : isHandlerRecord.trim();
    }
}
