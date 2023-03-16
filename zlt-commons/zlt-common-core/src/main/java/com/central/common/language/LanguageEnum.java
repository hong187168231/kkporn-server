package com.central.common.language;

public enum LanguageEnum {
    ZH(0, "zh", "zh", "中文"),
    EN(1, "en", "en", "英文"),
    KH(2, "kh", "kh", "高棉语"),
    TH(3, "th", "th", "泰语"),
    VI(4, "vi", "vi", "越南语"),
    MY(5, "my", "my", "马来语");

    private Integer code;
    private String value;
    private String packageKey;
    private String description;

    LanguageEnum(Integer code, String value, String packageKey, String description) {
        this.code = code;
        this.value = value;
        this.packageKey = packageKey;
        this.description = description;
    }

    public static LanguageEnum getByCode(Integer code) {
        for (LanguageEnum e : LanguageEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return EN;
    }

    public static LanguageEnum getByValue(String value) {
        for (LanguageEnum e : LanguageEnum.values()) {
            if (e.value.equalsIgnoreCase(value)) {
                return e;
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

    public Integer getCode() {
        return code;
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
