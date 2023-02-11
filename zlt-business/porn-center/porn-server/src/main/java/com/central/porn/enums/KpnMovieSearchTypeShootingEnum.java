package com.central.porn.enums;

import com.central.porn.core.language.LanguageEnum;
import com.central.porn.core.language.LanguageThreadLocal;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 拍摄性质
 */
public enum KpnMovieSearchTypeShootingEnum {
    Special(0, "专业拍摄", "professional", "វិជ្ជាជីវៈ"),
    Sneak(1, "偷拍", "sneak shot", "ការបាញ់ប្រហារ"),
    Selfie(2, "自拍", "selfie", "សែលហ្វី"),
    Other(3, "其他", "other", "ផ្សេងទៀត"),
    ;
    private Integer code;
    private String nameZh;
    private String nameEn;
    private String nameKh;

    KpnMovieSearchTypeShootingEnum(Integer code, String nameZh, String nameEn, String nameKh) {
        this.code = code;
        this.nameZh = nameZh;
        this.nameEn = nameEn;
        this.nameKh = nameKh;
    }

    public static Map<Integer, String> getOptions() {
        final String language = LanguageThreadLocal.getLanguage();

        return Arrays.stream(values()).collect(Collectors.toMap(o -> o.code, o -> {
            if (language.equalsIgnoreCase(LanguageEnum.ZH.name().toLowerCase())) {
                return o.nameZh;
            }

            if (language.equalsIgnoreCase(LanguageEnum.EN.name().toLowerCase())) {
                return o.nameEn;
            }

            if (language.equalsIgnoreCase(LanguageEnum.KH.name().toLowerCase())) {
                return o.nameKh;
            }
            return o.nameEn;
        }));
    }
}
