package com.jsh.erp.datasource.vo;

import java.math.BigDecimal;
import java.util.Date;

public class CustomerAnalysisVo {

    private Long customerId;

    private String customerName;

    private String phone;

    private String customerLevel;

    private String customerSource;

    private Integer orderCount;

    private BigDecimal totalAmount;

    private BigDecimal avgAmount;

    private Date lastOrderTime;

    private Integer daysSinceLastOrder;

    private String valueLevel;

    private Integer followUpCount;

    private Date lastFollowUpTime;

    private String followUpStatus;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(String customerSource) {
        this.customerSource = customerSource;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAvgAmount() {
        return avgAmount;
    }

    public void setAvgAmount(BigDecimal avgAmount) {
        this.avgAmount = avgAmount;
    }

    public Date getLastOrderTime() {
        return lastOrderTime;
    }

    public void setLastOrderTime(Date lastOrderTime) {
        this.lastOrderTime = lastOrderTime;
    }

    public Integer getDaysSinceLastOrder() {
        return daysSinceLastOrder;
    }

    public void setDaysSinceLastOrder(Integer daysSinceLastOrder) {
        this.daysSinceLastOrder = daysSinceLastOrder;
    }

    public String getValueLevel() {
        return valueLevel;
    }

    public void setValueLevel(String valueLevel) {
        this.valueLevel = valueLevel;
    }

    public Integer getFollowUpCount() {
        return followUpCount;
    }

    public void setFollowUpCount(Integer followUpCount) {
        this.followUpCount = followUpCount;
    }

    public Date getLastFollowUpTime() {
        return lastFollowUpTime;
    }

    public void setLastFollowUpTime(Date lastFollowUpTime) {
        this.lastFollowUpTime = lastFollowUpTime;
    }

    public String getFollowUpStatus() {
        return followUpStatus;
    }

    public void setFollowUpStatus(String followUpStatus) {
        this.followUpStatus = followUpStatus;
    }
}
