package com.central.porn.enums;

import com.central.porn.core.language.LanguageEnum;
import com.central.porn.core.language.LanguageThreadLocal;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum KpnMovieSearchTypePayEnum {
    Free("免费", "free", "ឥតគិតថ្លៃ"),
    Vip("VIP观看", "VIP only", "វីអាយភីតែប៉ុណ្ណោះ"),
    ;

    private String nameZh;
    private String nameEn;
    private String nameKh;

    KpnMovieSearchTypePayEnum(String nameZh, String nameEn, String nameKh) {
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
