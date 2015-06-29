package com.partner;

import android.app.Application;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.partner.common.constant.PreferenceConsts;
import com.partner.common.util.PreferenceUtils;
import com.partner.model.UserInfo;
import com.youdao.yjson.YJson;

public class PartnerApplication extends Application {

    private static PartnerApplication application = null;

    private TelephonyManager telephonyManager;

    private UserInfo userInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        Fresco.initialize(this);
        init();
    }

    public static PartnerApplication getInstance() {
        return application;
    }

    private void init() {
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    }

    public String getImei() {
       return telephonyManager.getDeviceId();
    }

    public void initUserInfo() {
        String info = PreferenceUtils.getString(PreferenceConsts.KEY_USER_INFO, null);
        if(!TextUtils.isEmpty(info)) {
            userInfo = YJson.getObj(info, UserInfo.class);
        }
    }
}
