package com.central.common.model.enums;

public enum SiteMovieStatusEnum {
    TO_PUBLISH(0, "待发布"),
    ON_SHELF(1, "上架"),
    OFF_SHELF(2, "下架"),
    ;

    private final Integer status;
    private final String remark;

    SiteMovieStatusEnum(Integer status, String remark) {
        this.status = status;
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public String getRemark() {
        return remark;
    }
}
