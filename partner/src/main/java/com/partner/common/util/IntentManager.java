package com.partner.common.util;

import android.content.Context;
import android.content.Intent;

import com.partner.activity.GuideActivity;
import com.partner.activity.login.InstitutionRegisterActivity;
import com.partner.activity.login.LoginActivity;
import com.partner.activity.MainActivity;
import com.partner.activity.login.ModifyPsdActivity;
import com.partner.activity.login.RegisterActivity;

/**
 * intent manager for the whole application
 * Created by yuym on 4/15/15.
 */
public class IntentManager {


    /**
     * start guide activity
     *
     * @param context
     */
    public static void startGuideActivity(Context context) {
        Intent intent = new Intent(context, GuideActivity.class);
        context.startActivity(intent);
    }

    /**
     * start login activity
     *
     * @param context
     */
    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * start register activity
     *
     * @param context
     */
    public static void startRegisterActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    /**
     * start institution register activity
     *
     * @param context
     */
    public static void startInstitutionRegisterActivity(Context context) {
        Intent intent = new Intent(context, InstitutionRegisterActivity.class);
        context.startActivity(intent);
    }

    /**
     * start main activity
     *
     * @param context
     */
    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * start modify psd activity
     *
     * @param context
     */
    public static void startModifyPsdActivity(Context context) {
        Intent intent = new Intent(context, ModifyPsdActivity.class);
        context.startActivity(intent);
    }
}
