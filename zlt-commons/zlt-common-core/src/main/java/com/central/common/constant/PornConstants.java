package com.central.common.constant;

/**
 * 常量类
 */
public final class PornConstants {

    //符号
    public static final class Symbol {
        public static final String ARRIVE = "-";
        public static final String ASTERISK = "*";
        public static final String SHARP = "#";
        public static final String COMMA = ",";
        public static final String COLON = ":";
        public static final String AT = "@";
        public static final String UNDERSCORE = "_";
        public static final String BIG_BRACKETS = "{}";
        public static final String APOSTROPHE = "'";
        public static final String DOUBLE_QUOTES = "\"";
        public static final String PERCENT = "%";
        public static final String CROSS = "^";//用于交叉下注
    }

    //模板
    public static final class Template {
    }

    //数字常量
    public static final class Numeric {
        // 30分钟的秒数
        public static final long MINUTE_30_IN_SECONDS = 30 * 60;
        // 主单号长度
        public static final int LEN_ORDER = 16;
        // 子单号长度
        public static final int LEN_DETAIL_ORDER = 22;
    }

    //字符串常量
    public static final class Str {
        public static final String EMPTY = "";
        public static final String SPACE = " ";
        public static final String ALL = "ALL";
        public static final String USERAGENT = "User-Agent";
        public static final String IPHONE = "iPhone";
        public static final String ANDROID = "Android";
        public static final String WINDOWS = "Windows";
        public static final String LINUX = "Linux";
        public static final String MACINTOSH = "Macintosh";
        public static final String PC = "PC";
        public static final String UNKNOWN = "unknown";
        public static final String RECORDS = "records";
        public static final String PAGE = "page";
        public static final String TOTAL = "total";
        public static final String Bearer = "Bearer";
        public static final String LANGUAGE = "language";
        public static final String DAY_BEGIN = " 00:00:00";
        public static final String DAY_END = " 23:59:59";
    }

    public static final class Format {
        public static final String yyyyMMdd = "yyyyMMdd";
        public static final String HHmmss = "HHmmss";
    }

    //redis
    public static final class RedisKey {
        //缓存时间
        public static final Long EXPIRE_TIME_30_DAYS = 30 * 24 * 60 * 60L;
        //缓存商户
        public static final String MERCHANT_DETAIL_KEY = "LtyMerchantDetail:{}";
        //缓存后台管理员ip
        public static final String BACKEND_WHITELIST_KEY = "Whitelist";
        // 城市
        public static final String CITY_KEY = "CITY:{}";
        // 盘口信息 HANDICAP:盘口名称
        public static final String HANDICAP_KEY = "HANDICAP:{}";
        // 城市,期号
        public static final String LOTTERY_OPEN_RESULT = "CITY:ISSUE:{}-{}";
        // 盘口设定明细
        public static final String HANDICAP_SETTINGS_DEL_PREFIX = "HandicapSettings:{}:*";
        // 盘口  HandicapSettings:10(handicap_id):2(section):2D(playType)
        public static final String HANDICAP_SETTINGS = "HandicapSettings:{}:{}:{}";
        // 系统维护key
        public static final String SYS_PLATFORM_CONFIG = "SYS_PLATFORM_CONFIG";


    }

    //sql
    public static final class Sql {
        public static final String LOCK_FOR_UPDATE = " FOR UPDATE";
        public static final String LOCK_IN_SHARE_MODE = " LOCK IN SHARE MODE";
    }

    //lock
    public static final class Lock {
        public static final Integer WAIT_TIME = 120;//等待 120s
        public static final Integer LEASE_TIME = 30;//自动释放 30s
        public static final String REDISSON_LOCK_CANCEL_BET = "REDISSON_LOCK_CANCEL:{}";
    }

    //多语言key
    public static final class LangKey {
    }
}
