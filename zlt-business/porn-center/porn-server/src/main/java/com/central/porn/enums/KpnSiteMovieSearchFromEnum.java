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
        return TOPIC.code.equals(code);
    }

    /**
     * 是否找片
     *
     * @param code 编码
     * @return
     */
    public static boolean isSearch(Integer code) {
        return SEARCH.code.equals(code);
    }


    public Integer getCode() {
        return code;
    }
}
