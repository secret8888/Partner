package com.partner;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class PartnerApplication extends Application {

    private static PartnerApplication application = null;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        Fresco.initialize(this);
    }

    public static PartnerApplication getInstance() {
        return application;
    }

}
