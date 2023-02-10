package com.central.porn.enums;

import com.central.porn.core.language.LanguageEnum;
import com.central.porn.core.language.LanguageThreadLocal;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum KpnMovieSearchTypeSubtitleEnum {
    Nothing("无字幕", "No subtitle", "គ្មានចំណងជើងរងទេ។"),
    Chinese("中文字幕", "Chinese subtitle", "អត្ថបទរឿងចិន"),
    English("英文字幕", "English subtitles", "អត្ថបទរឿងជាភាសាអង់គ្លេស"),
    Chinese_English("中英文字幕", "Chinese and English subtitles", "អត្ថបទរឿងចិន និងអង់គ្លេស"),
//    Cambodian("柬文字幕", "Khmer subtitles", "អត្ថបទរឿងខ្មែរ"),
    Other("其他字幕", "Other subtitles", "ចំណងជើងរងផ្សេងទៀត។"),
    ;

    private String nameZh;
    private String nameEn;
    private String nameKh;

    KpnMovieSearchTypeSubtitleEnum(String nameZh, String nameEn, String nameKh) {
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
