package com.central.porn.enums;

import com.central.porn.core.language.LanguageEnum;
import com.central.porn.core.language.LanguageThreadLocal;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public enum KpnMovieSearchTypeCountryEnum {
    Japan(0, "日本", "Japan", "ជប៉ុន"),
    China(1, "中国大陆", "China", "ចិន"),
    Taiwan(2, "中国台湾", "Taiwan", "តៃវ៉ាន់"),
    Korea(3, "韩国", "Korea", "កូរ៉េ"),
    EuropeanAmerican(4, "欧美", "European and american", "អឺរ៉ុប និងអាមេរិក"),
    SoutheastAsia(5, "东南亚", "Southeast Asia", "អាស៊ី\u200Bអា\u200Bគ្នេ\u200Bយ៏"),
    OtherAreas(6, "其他地区", "Other Areas", "តំបន់ផ្សេងទៀត។"),
    ;

    private Integer code;
    private String nameZh;
    private String nameEn;
    private String nameKh;

    KpnMovieSearchTypeCountryEnum(Integer code, String nameZh, String nameEn, String nameKh) {
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
