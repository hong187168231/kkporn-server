package com.central.porn.core.language;

import cn.hutool.core.util.StrUtil;

public enum LanguageEnum {
    EN("en", "en_us", "英文"),
    ZH("zh", "zh_cn", "中文"),
    KM("km", "khm", "高棉语"),
    TH("th", "th", "泰语"),
    VI("vi", "vi", "越南语");

    private String value;
    private String packageKey;
    private String description;

    LanguageEnum(String value, String packageKey, String description) {
        this.value = value;
        this.packageKey = packageKey;
        this.description = description;
    }

    public static LanguageEnum getByValue(String value) {
        for (LanguageEnum languageEnum : LanguageEnum.values()) {
            if (languageEnum.value.equalsIgnoreCase(value)) {
                return languageEnum;
            }
        }
        return EN;
    }

    public static boolean isZh(String language) {
        if (LanguageEnum.ZH.name().equalsIgnoreCase(language)) {
            return true;
        }
        return false;
    }


    public String getValue() {
        return value;
    }

    public String getPackageKey() {
        return packageKey;
    }

    public String getDescription() {
        return description;
    }
}
