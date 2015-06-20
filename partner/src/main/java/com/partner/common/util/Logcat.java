package com.partner.common.util;

import android.text.TextUtils;
import android.util.Log;

import com.partner.common.constant.Consts;

public class Logcat {

    /**
     * log.d
     * 
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
            return;
        }
        if (Consts.ON_DEBUG) {
            Log.d(tag, msg);
        }
    }

    /**
     * log.e
     * 
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
            return;
        }
        if (Consts.ON_DEBUG) {
            Log.e(tag, msg);
        }
    }

    /**
     * log.e
     * 
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg, Throwable throwable) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
            return;
        }
        if (Consts.ON_DEBUG) {
            Log.e(tag, msg, throwable);
        }
    }

    /**
     * log.w
     * 
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
            return;
        }
        if (Consts.ON_DEBUG) {
            Log.w(tag, msg);
        }
    }

    /**
     * Log.i
     * 
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
            return;
        }
        if (Consts.ON_DEBUG) {
            Log.i(tag, msg);
        }
    }

    /**
     * log.v
     * 
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
            return;
        }
        if (Consts.ON_DEBUG) {
            Log.v(tag, msg);
        }
    }
}
