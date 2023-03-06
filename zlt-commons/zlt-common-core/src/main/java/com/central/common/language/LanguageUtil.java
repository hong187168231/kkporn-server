package com.central.common.language;


import com.central.porn.entity.vo.LanguageNameMulti;
import org.springframework.stereotype.Component;

@Component
public class LanguageUtil {

//    private static LanguageProps languageProps;
//
//    public LanguageUtil() {
//    }
//
//    @Autowired
//    public void setLanguageProps(LanguageProps languageProps) {
//        LanguageUtil.languageProps = languageProps;
//    }
//
//
//    public LanguageUtil(LanguageProps languageProps) {
//        LanguageUtil.languageProps = languageProps;
//    }
//
//    public static String getMessage(String messageKey) {
//        final String language = LanguageThreadLocal.getLanguage();
//        return getLanguageMap(LanguageEnum.getByValue(language)).get(messageKey);
//    }

    public static String getLanguageName(LanguageNameMulti languageNameVo) {
        final String language = LanguageThreadLocal.getLanguage();
        String name = languageNameVo.getNameEn();
        if (language.equalsIgnoreCase(LanguageEnum.ZH.name().toLowerCase())) {
            name = languageNameVo.getNameZh();
        }

        else if (language.equalsIgnoreCase(LanguageEnum.EN.name().toLowerCase())) {
            name = languageNameVo.getNameEn();
        }

        else if (language.equalsIgnoreCase(LanguageEnum.KH.name().toLowerCase())) {
            name = languageNameVo.getNameKh();
        }
        languageNameVo.setNull();
        return name;
    }

//    private static Map<String, String> getLanguageMap(LanguageEnum language) {
//        switch (language) {
//            case ZH:
//                return languageProps.getZh();
//            case KH:
//                return languageProps.getKhm();
//            case TH:
//                return languageProps.getTh();
//            case VI:
//                return languageProps.getVi();
//            case EN:
//            default:
//                return languageProps.getEn();
//        }
//    }

}
