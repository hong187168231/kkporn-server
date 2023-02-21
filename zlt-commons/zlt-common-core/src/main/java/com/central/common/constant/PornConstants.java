package com.central.common.constant;

import cn.hutool.core.util.RandomUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //开
        public static final int OPEN = 1;
        //关
        public static final int CLOSE = 0;
        // 30分钟的秒数
        public static final long MINUTE_30_IN_SECONDS = 30 * 60;
        // 订单号长度
        public static final int LEN_DETAIL_ORDER = 20;
        // 默认排序sort
        public static final long DEFAULT_SORT_VALUE = 0L;
    }

    //正则表达式
    public static final class Reg {
        /**账号校验*/
        public static final String CHECK_USERNAME = "^[a-zA-Z0-9]{6,10}$";
        /**密码校验*/
        public static final String CHECK_PASSWORD = "^[a-zA-Z0-9]{6,20}$";

    }

    //字符串常量
    public static final class Str {
        public static final String EMPTY = "";
        public static final String SPACE = " ";
        public static final String ALL = "ALL";
        public static final String RANDOM_BASE_STR = RandomUtil.BASE_CHAR_NUMBER+"ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        public static final String VERIFY_CODE_ID = "VerifyCodeId";
        public static final String VERIFY_CODE_SECONDS = "VerifyCodeSeconds";
        public static final String USER_AGENT = "User-Agent";
        public static final String LOGIN_IP = "login_ip";
        public static final String REHOST = "reHost";
        public static final String SID = "sid";
        public static final String Host = "Host";
        public static final String REFERER = "Referer";
        public static final String COUNTRY = "country";
        public static final String SHOOTING = "shooting";
        public static final String SUBTITLE = "subtitle";
        public static final String PAY = "pay";
        public static final String ACCESS_TOKEN = "access_token";
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
    public static final class LocalCache {
        //{kye=movieid,value=[namezh,nameen,namekh]}
        public static final Map<Long, String[]> LOCAL_MAP_MOVIE_NAME = new HashMap<>();
        //{key=站点id,value=[movieId01,movieId02,....,movieIdn]}
        public static final Map<Long, List<Long>> LOCAL_MAP_SITE_MOVIE_IDS = new HashMap<>();
    }

    //redis
    public static final class RedisKey {
        //缓存时间 1分钟
        public static final Long EXPIRE_TIME_1_MINUTE = 1 * 60L;
        //缓存时间 30天
        public static final Long EXPIRE_TIME_30_DAYS = 30 * 24 * 60 * 60L;
        //缓存时间 30天
        public static final Long EXPIRE_TIME_7_DAYS = 7 * 24 * 60 * 60L;
        //缓存时间 1天
        public static final Long EXPIRE_TIME_1_DAYS = 1 * 24 * 60 * 60L;
        //缓存所有站点信息
        public static final String SITE_LIST_KEY = "SITE:LIST";
        //缓存站点信息 siteid
        public static final String SITE_INFO_KEY = "SITE:INFO:{}";
        //缓存站点频道信息 siteid
        public static final String SITE_STASH_CHANNEL_KEY = "SITE:CHANNEL:{}";
        //缓存站点专题信息 siteid
        public static final String SITE_TOPIC_KEY = "SITE:TOPIC:{}";
        //缓存站点专题影片id信息 list类型  siteid,topicid
        public static final String SITE_TOPIC_MOVIEID_SORT_KEY = "SITE:TOPIC:MOVIEID:SORT:{}:{}";
        //缓存影片VO信息 str movieid
        public static final String KPN_MOVIEID_VO_KEY = "KPN:MOVIEID:VO:{}";
        //缓存影片标签信息 list movieid
        public static final String KPN_MOVIEID_TAGS_KEY = "KPN:MOVIEID:TAGS:{}";
        //缓存影片标签信息 str actorid
        public static final String KPN_ACTOR_KEY = "KPN:ACTOR:{}";
        //缓存标签信息 str tagid
        public static final String KPN_TAGID_KEY = "KPN:TAGID:{}";
        //缓存影片播放量 siteid,movieid
        public static final String KPN_SITE_MOVIE_VV_KEY = "KPN:SITEID:MOVIEID:VV:{}:{}";
        //缓存影片收藏量 siteid,movieid
        public static final String KPN_SITE_MOVIE_FAVORITES_KEY = "KPN:SITEID:MOVIEID:FAVORITES:{}:{}";
        //缓存站点演员收藏量 siteid,actorid
        public static final String KPN_SITE_ACTOR_FAVORITES_KEY = "KPN:SITEID:ACTORID:FAVORITES:{}:{}";
        //缓存站点演员影片量 siteid,actorid
        public static final String KPN_SITE_ACTOR_MOVIENUM_KEY = "KPN:SITEID:ACTORID:MOVIENUM:{}:{}";
        //缓存线路信息
        public static final String KPN_LINE = "KPN:LINE";
        //缓存站点月播放量排行 list siteid
        public static final String KPN_SITE_MONTH_MOVIE_KEY = "KPN:SITEID:MONTH:MOVIES:{}";
        //缓存站点搜索关键词周排行
        public static final String KPN_SITE_SEARCH_WEEK_KEY = "KPN:SITE:SEARCH:WEEK:{}";
        //缓存站点搜索关键词总排行
        public static final String KPN_SITE_SEARCH_TOTAL_KEY = "KPN:SITE:SEARCH:TOTAL:{}";

        //缓存站点标签影片id排序 播放量排序 高->低  SITEID,TAGID
        public static final String KPN_SITE_TAG_MOVIEID_VV = "KPN:SITE:TAG:MOVIEID:VV:{}:{}";

        //缓存站点标签影片id排序 时长排序 长->短 SITEID,TAGID
        public static final String KPN_SITE_TAG_MOVIEID_DURATION = "KPN:SITE:TAG:MOVIEID:DURATION:{}:{}";

        //缓存站点标签影片id排序 创建时间 新->旧 SITEID,TAGID
        public static final String KPN_SITE_TAG_MOVIEID_LATEST = "KPN:SITE:TAG:MOVIEID:LATEST:{}:{}";

        //缓存站点专题影片id排序 播放量排序 高->低  SITEID,TOPICID
        public static final String KPN_SITE_TOPIC_MOVIEID_VV = "KPN:SITE:TOPIC:MOVIEID:VV:{}:{}";

        //缓存站点专题影片id排序 时长排序 长->短 SITEID,TOPICID
        public static final String KPN_SITE_TOPIC_MOVIEID_DURATION = "KPN:SITE:TOPIC:MOVIEID:DURATION:{}:{}";

        //缓存站点专题影片id排序 创建时间 新->旧 SITEID,TOPICID
        public static final String KPN_SITE_TOPIC_MOVIEID_LATEST = "KPN:SITE:TOPIC:MOVIEID:LATEST:{}:{}";

        //缓存站点频道影片播放量排序 高->低  SITEID,CHANNELID
        public static final String KPN_SITE_CHANNEL_MOVIEID_VV = "KPN:SITE:CHANNEL:MOVIEID:VV:{}:{}";

        //缓存站点频道影片时长排序 长->短 SITEID,CHANNELID
        public static final String KPN_SITE_CHANNEL_MOVIEID_DURATION = "KPN:SITE:CHANNEL:MOVIEID:DURATION:{}:{}";

        //缓存站点频道影片创建时间 新->旧 SITEID,CHANNELID
        public static final String KPN_SITE_CHANNEL_MOVIEID_LATEST = "KPN:SITE:CHANNEL:MOVIEID:LATEST:{}:{}";

        //缓存站点最新影片 上架时间排序 新->旧  SITEID
        public static final String KPN_SITE_ALL_MOVIEID_LATEST = "KPN:SITE:ALL:MOVIEID:LATEST:{}";

        //缓存站点最热影片 播放量排序 高->低  SITEID
        public static final String KPN_SITE_ALL_MOVIEID_VV = "KPN:SITE:ALL:MOVIEID:VV:{}";

        //缓存站点最热影片 播放量排序 高->低  SITEID
        public static final String KPN_SITE_VIP_MOVIEID_VV = "KPN:SITE:VIP:MOVIEID:VV:{}";

        //缓存会员频道信息 userid
        public static final String SITE_USER_CHANNEL_KEY = "SITE:USER:CHANNEL:{}";

        //站点影片信息变动标识 默认0,删除-1,增加1
        public static final String SITE_MOVIE_CHANGE_FLAG = "SITE:MOVIE:CHANGE:FLAG:{}";


        //缓存后台管理员ip
        public static final String BACKEND_WHITELIST_KEY = "Whitelist";
        // 系统维护key
        public static final String SYS_PLATFORM_CONFIG = "SYS_PLATFORM_CONFIG";
        //访问量
        public static final String KPN_PV_KEY = "KPN:PV:{}";
        //独立访客数
        public static final String KPN_UV_KEY = "KPN:UV:{}";
    }
    //sql
    public static final class Sql {
        public static final String LOCK_FOR_UPDATE = " FOR UPDATE";
        public static final String LOCK_IN_SHARE_MODE = " LOCK IN SHARE MODE";
        public static final String COLUMN_VV = "`vv`";
        public static final String COLUMN_DURATION = "`duration`";
        public static final String COLUMN_CREATE_TIME = "`create_time`";
    }

    //lock
    public static final class Lock {
        public static final Integer WAIT_TIME = 120;//等待 120s
        public static final Integer LEASE_TIME = 30;//自动释放 30s
    }

    //多语言key
    public static final class LangKey {
    }
}
