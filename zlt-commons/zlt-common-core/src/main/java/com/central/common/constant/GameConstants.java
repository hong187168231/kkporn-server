package com.central.common.constant;

/**
 * 只存游戏相关数据常量，
 * 和游戏无关的常量，
 * 建议不要放
 *
 */
public interface GameConstants {

    /**
     * 正常
     */
    Integer TESTER_STATUS_ON = 1;

    /**
     * 关闭
     */
    Integer TESTER_STATUS_OFF = 0;

    /**
     * 正常
     */
    Integer TESTER_STATUS_BLOCK = 2;


    public class KeyWords{
        private KeyWords(){}
        public static final String SUFFIX_LOCK = "_lock";
        public static final String PLATFORM_CATEGORY_LIST = "PlatformGame.gameCode";
        public static final String PLATFORM_LANGUAGE_LIST = "PlatformGame.language";

    }

}
