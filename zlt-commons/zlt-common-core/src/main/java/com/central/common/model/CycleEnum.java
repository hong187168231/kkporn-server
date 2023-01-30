package com.central.common.model;

import com.central.common.constant.I18nKeys;
import lombok.Getter;

/**
 * 周期相关枚举
 */
public enum CycleEnum {

    MONDAY(1, "周一","Monday","ថ្ងៃច័ន្ទ"),
    TUESDAY(2, "周二","Tuesday","ថ្ងៃអង្គារ"),
    WEDNESDAY(3, "周三","Wednesday","ថ្ងៃពុធ"),
    THURSDAY(4, "周四","Thursday","ថ្ងៃព្រហស្បតិ៍"),
    FRIDAY(5, "周五","Friday","ថ្ងៃសុក្រ"),
    SATURDAY(6, "周六","Saturday","ថ្ងៃសៅរ៍"),
    SUNDAY(7, "周日","Sunday","ថ្ងៃអាទិត្យ");

    @Getter
    private final int type;

    @Getter
    private final String name;
    @Getter
    private final String enUs;
    @Getter
    private final String khm;


    CycleEnum(int type, String name, String enUs, String khm){
        this.type = type;
        this.name = name;
        this.enUs = enUs;
        this.khm = khm;
    }

    public static String fingCycleEnumType(Integer type,String language){
        for(CycleEnum capitalEnum : CycleEnum.values()){
            if(capitalEnum.type == type){
                if (language.equals(I18nKeys.Locale.ZH_CN)){
                    return capitalEnum.name;
                }
                if (language.equals(I18nKeys.Locale.EN_US)){
                    return capitalEnum.enUs;
                }
                if (language.equals(I18nKeys.Locale.KHM)){
                    return capitalEnum.khm;
                }
            }
        }
        return String.valueOf(type);
    }

}
