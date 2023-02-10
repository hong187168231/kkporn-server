package com.central.porn.enums;

import com.central.porn.core.language.LanguageEnum;
import com.central.porn.core.language.LanguageThreadLocal;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum KpnMovieSearchTypeCountryEnum {
    Japan("日本", "Japan", "ជប៉ុន"),
    China("中国大陆", "China", "ចិន"),
    Taiwan("中国台湾", "Taiwan", "តៃវ៉ាន់"),
    Korea("韩国", "Korea", "កូរ៉េ"),
    EuropeanAmerican("欧美", "European and american", "អឺរ៉ុប និងអាមេរិក"),
    SoutheastAsia("东南亚", "Southeast Asia", "អាស៊ី\u200Bអា\u200Bគ្នេ\u200Bយ៏"),
    OtherAreas("其他地区", "Other Areas", "តំបន់ផ្សេងទៀត។"),
    ;

    private String nameZh;
    private String nameEn;
    private String nameKh;

    KpnMovieSearchTypeCountryEnum(String nameZh, String nameEn, String nameKh) {
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
