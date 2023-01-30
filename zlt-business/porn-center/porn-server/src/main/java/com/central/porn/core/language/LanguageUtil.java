package com.central.porn.core.language;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LanguageUtil {

    private static LanguageProps languageProps;

    public LanguageUtil() {
    }

    @Autowired
    public void setLanguageProps(LanguageProps languageProps) {
        LanguageUtil.languageProps = languageProps;
    }


    public LanguageUtil(LanguageProps languageProps) {
        LanguageUtil.languageProps = languageProps;
    }

    public static String getMessage(String messageKey) {
        final String language = LanguageThreadLocal.getLanguage();
        return getLanguageMap(LanguageEnum.getByValue(language)).get(messageKey);
    }

    private static Map<String, String> getLanguageMap(LanguageEnum language) {
        switch (language) {
            case ZH:
                return languageProps.getZh();
            case KM:
                return languageProps.getKm();
            case TH:
                return languageProps.getTh();
            case VI:
                return languageProps.getVi();
            case EN:
            default:
                return languageProps.getEn();
        }
    }

}
