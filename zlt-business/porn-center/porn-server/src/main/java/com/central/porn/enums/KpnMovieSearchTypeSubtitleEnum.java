package com.central.porn.enums;

import com.central.common.language.LanguageEnum;
import com.central.common.language.LanguageThreadLocal;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum KpnMovieSearchTypeSubtitleEnum {
    Nothing(0, "无字幕", "No subtitle", "គ្មានចំណងជើងរងទេ។"),
    Chinese(1, "中文字幕", "Chinese subtitle", "អត្ថបទរឿងចិន"),
    English(2, "英文字幕", "English subtitles", "អត្ថបទរឿងជាភាសាអង់គ្លេស"),
    Chinese_English(3, "中英文字幕", "Chinese and English subtitles", "អត្ថបទរឿងចិន និងអង់គ្លេស"),
    //    Cambodian("柬文字幕", "Khmer subtitles", "អត្ថបទរឿងខ្មែរ"),
    Other(4, "其他字幕", "Other subtitles", "ចំណងជើងរងផ្សេងទៀត។"),
    ;

    private Integer code;
    private String nameZh;
    private String nameEn;
    private String nameKh;

    KpnMovieSearchTypeSubtitleEnum(Integer code, String nameZh, String nameEn, String nameKh) {
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
