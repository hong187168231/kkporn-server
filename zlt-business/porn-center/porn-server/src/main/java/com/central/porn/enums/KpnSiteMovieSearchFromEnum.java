package com.central.porn.enums;

public enum KpnSiteMovieSearchFromEnum {
    //    0:找片,1:标签,2:专题,3:频道,4:热门VIP推荐
    SEARCH(0, "找片"),
    TAG(1, "标签"),
    TOPIC(2, "专题"),
    CHANNEL(3, "频道"),
    VIP_RECOMMEND(4, "热门VIP推荐"),
    LATEST(5, "最新"),
    HOTTEST(6, "最热"),
    ;

    private Integer code;
    private String remark;

    KpnSiteMovieSearchFromEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public static boolean isTopic(Integer code) {
        for (KpnSiteMovieSearchFromEnum e : values()) {
            if (e.code.equals(code)) {
                return true;
            }
        }
        return false;
    }

    public Integer getCode() {
        return code;
    }
}
