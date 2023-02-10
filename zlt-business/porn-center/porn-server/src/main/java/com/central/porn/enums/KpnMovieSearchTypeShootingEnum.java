package com.central.porn.enums;

import com.central.porn.core.language.LanguageEnum;
import com.central.porn.core.language.LanguageThreadLocal;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 拍摄性质
 */
public enum KpnMovieSearchTypeShootingEnum {
    Special("专业拍摄", "professional", "វិជ្ជាជីវៈ"),
    Sneak("偷拍", "sneak shot", "ការបាញ់ប្រហារ"),
    Selfie("自拍", "selfie", "សែលហ្វី"),
    Other("其他", "other", "ផ្សេងទៀត"),
    ;

    private String nameZh;
    private String nameEn;
    private String nameKh;

    KpnMovieSearchTypeShootingEnum(String nameZh, String nameEn, String nameKh) {
        this.nameZh = nameZh;
        this.nameEn = nameEn;
        this.nameKh = nameKh;
    }

    public static List<String> getOptions() {
        final String language = LanguageThreadLocal.getLanguage();

        return Arrays.stream(values()).map(e -> {
            if (language.equalsIgnoreCase(LanguageEnum.ZH.name().toLowerCase())) {
                return e.nameZh;
            }

            if (language.equalsIgnoreCase(LanguageEnum.EN.name().toLowerCase())) {
                return e.nameEn;
            }

            if (language.equalsIgnoreCase(LanguageEnum.KH.name().toLowerCase())) {
                return e.nameKh;
            }
            return e.nameEn;
        }).collect(Collectors.toList());
    }
}
