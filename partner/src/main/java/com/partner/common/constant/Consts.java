package com.partner.common.constant;

import android.os.Environment;

import com.partner.BuildConfig;

/**
 * Created by yuym on 4/2/15.
 */
public class Consts {

    /**
     * 是否是调试模式
     */
    public static boolean ON_DEBUG = BuildConfig.DEBUG;

    /**
     * 是否是测试模式
     */
    public static boolean ON_TEST_MODEl = false;

    /**
     * log tag
     */
    public static String LOG_TAG = "Partner";

    /**
     * File Path *
     */
    public static final String ASSET_PATH_PREFIX = "file:///android_asset/";

    public static final String SDCARD_PATH_PREFIX = "file:///mnt/sdcard/";
    /*
     * 本地文件路径
     */
    public static final String MAIN_FILE_PATH = Environment.getExternalStorageDirectory() + "/Partner/partner/";
    public static final String TEMP_FILE_PATH = MAIN_FILE_PATH + "temp/";
    public static final String AVATAR_CACHE_FILE =  "avatar.png";
    public static final String PLATFORM_SHARE_NAME = "share_temp.png";
    public static final String IMAGE_CACHE_FILE =  "image.png";

    /*
     * 网络连接超时时间
     */
    public final static int TIMEOUT = 30 * 1000;

    /*
     * 退出间隔
     */
    public static final int INTERVAL = 2000;

    /*
     * page offset
     */
    public static final int PAGE_OFFSET = 20;

    /*
     * 临时文件
     */
    public final static String TEMP_NAME = "temp.jpg";
    public static final String SHARE_CACHE_NAME = "share_tmp.png";

    /**
     * 使用sso登陆需要用到的平台key等信息*
     */
    public static final String WEIBO_APP_KEY = "1102764931";

    public static final String QQ_APP_ID = "1104741285";

    public final static String WX_APP_ID = "wx2a268bdce9ba293a";

    /**
     * 用户角色, 0为普通用户，1为商家
     */
    public final static int ROLE_USER = 0;
    public final static int ROLE_BUSINESS = 1;

}
