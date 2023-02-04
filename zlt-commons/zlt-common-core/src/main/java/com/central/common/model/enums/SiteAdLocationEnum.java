package com.central.common.model.enums;

/**
 * 广告位置
 */
public enum SiteAdLocationEnum {
    BANNER(1, "首页-轮播图"),
    PLATFORM(2, "首页-平台图"),
    TOPIC(3, "首页-专题广告"),
    BOON(4, "福利"),
    GAME_BANNER(5, "游戏-轮播图"),
    GAME_AD(6, "游戏-广告"),
    ;

    private final Integer code;
    private final String remark;

    SiteAdLocationEnum(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public Integer getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }
}