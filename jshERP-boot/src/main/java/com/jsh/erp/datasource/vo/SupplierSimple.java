package com.jsh.erp.datasource.vo;

import lombok.Data;

@Data
public class SupplierSimple {

    private Long id;

    private String supplier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

}
