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
        // 订单号长度
        public static final int LEN_DETAIL_ORDER = 20;
        // 默认排序sort
        public static final long DEFAULT_SORT_VALUE = 0L;
    }

    //字符串常量
    public static final class Str {
        public static final String EMPTY = "";
        public static final String SPACE = " ";
        public static final String ALL = "ALL";
        public static final String USERAGENT = "User-Agent";
        public static final String LOGINIP = "LOGINIP";
        public static final String REHOST = "reHost";
        public static final String SID = "sid";
        public static final String Host = "Host";
        public static final String REFERER = "Referer";
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
        //缓存时间 30天
        public static final Long EXPIRE_TIME_30_DAYS = 30 * 24 * 60 * 60L;
        //缓存时间 30天
        public static final Long EXPIRE_TIME_7_DAYS = 7 * 24 * 60 * 60L;
        //缓存时间 1天
        public static final Long EXPIRE_TIME_1_DAYS = 1 * 24 * 60 * 60L;
        //缓存站点信息 siteid
        public static final String SITE_INFO_KEY = "SITE:INFO:{}";
        //缓存站点频道信息 siteid
        public static final String SITE_CHANNEL_KEY = "SITE:CHANNEL:{}";
        //缓存站点广告信息 siteid
        public static final String SITE_ADVERTISE_KEY = "SITE:AD:{}";
        //缓存站点专题信息 siteid
        public static final String SITE_TOPIC_KEY = "SITE:TOPIC:{}";
        //缓存站点专题影片id信息 list类型  siteid,topicid
        public static final String SITE_TOPIC_MOVIEID_KEY = "SITE:TOPIC:MOVIEID:{}:{}";
        //缓存影片信息 str movieid
        public static final String KPN_MOVIEID_KEY = "KPN:MOVIEID:{}";
        //缓存影片标签信息 list movieid
        public static final String KPN_MOVIEID_TAGS_KEY = "KPN:MOVIEID:TAGS:{}";
        //缓存影片标签信息 str actorid
        public static final String KPN_ACTOR_KEY = "KPN:ACTOR:{}";
        //缓存标签信息 str tagid
        public static final String KPN_TAGID_KEY = "KPN:TAGID:{}";
        //缓存影片播放量 siteid,movieid
        public static final String KPN_SITE_MOVIE_VV_KEY = "KPN:SITEID:MOVIEID:VV:{}:{}";
        //缓存影片收藏量 siteid,movieid
        public static final String KPN_SITE_MOVIE_FAVORITES_KEY = "KPN:SITEID:MOVIEID:Favorites:{}:{}";
        //缓存站点演员收藏量 siteid,actorid
        public static final String KPN_SITE_ACTOR_FAVORITES_KEY="KPN:SITEID:ACTORID:{}:{}";
        //缓存线路信息
        public static final String KPN_LINE = "KPN:LINE";

        //缓存会员频道信息 userid
        public static final String SITE_USER_CHANNEL_KEY = "SITE:USER:CHANNEL:{}";
        //缓存后台管理员ip
        public static final String BACKEND_WHITELIST_KEY = "Whitelist";
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
