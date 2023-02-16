package com.central.porn.enums;

public enum KpnSiteMovieSearchFromEnum {
    //    0:找片,1:标签,2:专题,3:频道,4:热门VIP推荐
    SEARCH(0, "找片"),
    TAG(1, "标签"),
    TOPIC(2, "专题"),
    CHANNEL(3, "频道"),
    VIP_RECOMMEND(4, "热门VIP推荐"),
    ;

    private Integer code;
    private String remark;

    KpnSiteMovieSearchFromEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public Integer getCode() {
        return code;
    }
}
