package com.central.porn.enums;

public enum KpnMovieSortOrderEnum {
    ASC(0, "正序"),
    DESC(1, "倒序"),
    ;

    //排序类型
    private Integer code;
    //描述
    private String remark;

    KpnMovieSortOrderEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public static boolean isLegalCode(Integer code) {
        for (KpnMovieSortOrderEnum orderEnum : values()) {
            if (orderEnum.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAsc(Integer code) {
        return code.equals(ASC.code);
    }

    public Integer getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }
}
