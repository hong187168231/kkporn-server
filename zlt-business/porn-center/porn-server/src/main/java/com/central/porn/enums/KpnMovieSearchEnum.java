package com.central.porn.enums;

public enum KpnMovieSearchEnum {

    HOT("HOT", "最热-观影次数"),
    LATEST("LATEST", "最新-上架时间"),
    TIME("TIME", "时长-影片时长"),
    ;

    //排序类型
    private String type;
    //描述
    private String remark;

    KpnMovieSearchEnum(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    public String getType() {
        return type;
    }
}
