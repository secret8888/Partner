/**
 * 
 */
package com.partner.common.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @author emilyu
 * @category toast 封装
 * @version 1.0.0
 */
public class Toaster {

    public static void show(Context context, int msgId) {
        Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show();
    }

    public static void showMsg(Context context, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showMsgShort(Context context, int msgId) {
        Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show();
    }

    public static void showMsgLong(Context context, int msgId) {
        Toast.makeText(context, msgId, Toast.LENGTH_LONG).show();
    }

    public static void showMsgShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showMsgLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showMsgLong(Context context, int msgId1, int msgId2) {
        String msg = context.getString(msgId1) + context.getString(msgId2);
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showMsgShort(Context context, int msgId1, int msgId2) {
        String msg = context.getString(msgId1) + context.getString(msgId2);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

    }

    public static void showMsgLong(Context context, int msgId1, String msg2) {
        String msg = context.getString(msgId1) + msg2;
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

}
