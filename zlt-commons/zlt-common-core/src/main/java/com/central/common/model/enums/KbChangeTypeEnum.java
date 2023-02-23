package com.central.common.model.enums;

import lombok.Getter;

/**
 * K币账变记录相关枚举
 */
public enum KbChangeTypeEnum {
    OPEN_VIP(1, "开通vip",-1),
    SIGN_REWARD(2, "签到奖励",1),
    FILL_INVITE_CODE(3, "填写邀请码奖励",1),
    PROMOTION(4, "推广获取奖励",1),;

    private final int type;

    private final String name;

    /**
     * 1.加钱，-1.减钱
     */
    @Getter
    private final int addOrSub;

    KbChangeTypeEnum(int type, String name, int addOrSub){
        this.type = type;
        this.name = name;
        this.addOrSub = addOrSub;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getAddOrSub() {
        return addOrSub;
    }


    public static String getTypeName(Integer type) {
        for (KbChangeTypeEnum value : values()) {
            if(value.type == type){
                return value.name;
            }
        }
        return null;
    }

}
