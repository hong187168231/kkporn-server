package com.central.common.model.enums;

/**
 * 广告位置
 */
public enum SiteAdPlatformEnum {
    PC("PC"),
    H5("H5"),
    ;

    private final String platform;

    SiteAdPlatformEnum(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }
}
