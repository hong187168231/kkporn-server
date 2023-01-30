package com.central.backend.util;

import com.central.common.constant.I18nKeys;
import com.central.common.utils.I18nUtil;
import com.central.common.utils.ServletUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TranslateConst {

    public static final Map<String,String> betPlayTypes = new HashMap<>();

    static {
        betPlayTypes.put("HEAD","头");
        betPlayTypes.put("TAIL","尾");
        betPlayTypes.put("HEAD_TAIL","头+尾");
        betPlayTypes.put("GROUP_ALL","包组");
        betPlayTypes.put("GROUP_7","包组7");
        betPlayTypes.put("GROUP_HEAD_7","顶部包组7");
        betPlayTypes.put("GROUP_MIDDLE_7","中部包组7");
        betPlayTypes.put("GROUP_TAIL_7","底部包组7");

        betPlayTypes.put("A","A");
        betPlayTypes.put("B","B");
        betPlayTypes.put("C","C");
        betPlayTypes.put("D","D");
        betPlayTypes.put("ABCD","ABCD");
        betPlayTypes.put("GROUP_ALL_2","包组2");
    }

    public static final Map<String,String> no_zh_betPlayTypes = new HashMap<>();

    static {
        no_zh_betPlayTypes.put("HEAD","HEAD");
        no_zh_betPlayTypes.put("TAIL","TAIL");
        no_zh_betPlayTypes.put("HEAD_TAIL","HEAD_TAIL");
        no_zh_betPlayTypes.put("GROUP_ALL","GROUP_ALL");
        no_zh_betPlayTypes.put("GROUP_7","GROUP_7");
        no_zh_betPlayTypes.put("GROUP_HEAD_7","GROUP_HEAD_7");
        no_zh_betPlayTypes.put("GROUP_MIDDLE_7","GROUP_MIDDLE_7");
        no_zh_betPlayTypes.put("GROUP_TAIL_7","GROUP_TAIL_7");
        no_zh_betPlayTypes.put("A","A");
        no_zh_betPlayTypes.put("B","B");
        no_zh_betPlayTypes.put("C","C");
        no_zh_betPlayTypes.put("D","D");
        no_zh_betPlayTypes.put("ABCD","ABCD");
        no_zh_betPlayTypes.put("GROUP_ALL_2","GROUP_ALL_2");
    }

    public static final Map<String,String> citys = new HashMap<>();

    static {
        citys.put("TP","胡志明");
        citys.put("CM","金瓯");
        citys.put("VT","头顿");
        citys.put("DT","同塔");
        citys.put("DN","同奈");
        citys.put("CT","芹苴");
        citys.put("TN","西宁");
        citys.put("AG","安江");
        citys.put("VL","永隆");
        citys.put("BD","平阳");

        citys.put("LA","隆安");
        citys.put("TG","前江");
        citys.put("KG","建江");
        citys.put("BL","薄辽");
        citys.put("DAL","大叻");
        citys.put("ST","朔庄");
        citys.put("BTH","平顺");
        citys.put("TV","茶荣");
        citys.put("BP","平福");
        citys.put("HG","后江");

        citys.put("BT","槟椥");
        citys.put("KT","昆嵩");
        citys.put("PY","富安");
        citys.put("KH","庆和");
        citys.put("DL","多乐");
        citys.put("QB","广平");
        citys.put("DNA","岘港");
        citys.put("QN","广南");
        citys.put("BDI","平定");
        citys.put("QT","广治");

        citys.put("NT","宁顺");
        citys.put("DNO","达农");
        citys.put("GL","嘉莱");
        citys.put("TT","顺化");
        citys.put("QNG","广义");
        citys.put("HN","河内");
    }


    public static final Map<String,String> currencys = new HashMap<>();

    static {
        currencys.put("KHR","柬埔寨瑞尔");
        currencys.put("VND","越南盾");
        currencys.put("CNY","人民币");
        currencys.put("USD","美元");
        currencys.put("KRW","韩元");
        currencys.put("EUR","欧元");
        currencys.put("THB","泰铢");
        currencys.put("GBP","英镑");
        currencys.put("JPY","日元");
        currencys.put("INR","印度卢比");
        currencys.put("MYR","马来西亚令吉");
        currencys.put("IDR","印尼盾");
        currencys.put("MMK","缅甸币");
    }

//    public static String getBetPlayType(String language,String key){
//        if (StringUtils.isBlank(key)){
//            return key;
//        }
//        if (I18nKeys.Locale.ZH_CN.equalsIgnoreCase(language)) {
//            return TranslateConst.betPlayTypes.get(key);
//        }
//        if (I18nKeys.Locale.EN_US.equalsIgnoreCase(language)) {
//            return key;
//        }
//        return key;
//    }

    public static String getBetPlayType(String key){
        if (StringUtils.isBlank(key)){
            return key;
        }
        key = TranslateConst.betPlayTypes.get(key);
        return I18nUtil.getBackendValue(key);
    }

    public static String getBetCity(String language,String key){
        if (StringUtils.isBlank(key)){
            return key;
        }
        if (I18nKeys.Locale.ZH_CN.equalsIgnoreCase(language)) {
            return TranslateConst.citys.get(key);
        }
        if (I18nKeys.Locale.EN_US.equalsIgnoreCase(language)) {
            return key;
        }
        return key;
    }

    public static String getCurrency(String language,String key){
        if (StringUtils.isBlank(key)){
            return key;
        }
        if (I18nKeys.Locale.ZH_CN.equalsIgnoreCase(language)) {
            return TranslateConst.currencys.get(key);
        }
        if (I18nKeys.Locale.EN_US.equalsIgnoreCase(language)) {
            return key;
        }
        return key;
    }

    public static String getLanguage(){
        HttpServletRequest request = ServletUtil.getHttpServletRequest();
        if (Objects.isNull(request)) {
            return I18nKeys.Locale.ZH_CN;
        }
        String language = request.getHeader(I18nKeys.LANGUAGE);
        if (StringUtils.isBlank(language)){
            return I18nKeys.Locale.ZH_CN;
        }
        return language;
    }
}
