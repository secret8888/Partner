package com.partner.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.constant.PreferenceConsts;
import com.partner.common.util.IntentManager;
import com.partner.common.util.PreferenceUtils;

public class SplashActivity extends BaseActivity {
    
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void readIntent() {

    }
    
    @Override
    protected void initControls(Bundle savedInstanceState) {  
    	handler.sendEmptyMessageDelayed(0, 1500);
    }
    
    @Override
    protected void setListeners() {
    }

    private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			boolean isFirstLogin = PreferenceUtils.getBoolean(PreferenceConsts.KEY_FIRST_LOGIN, true);
			if(isFirstLogin){
				IntentManager.startGuideActivity(SplashActivity.this);
			}else{
				String userToken = PreferenceUtils.getString(PreferenceConsts.KEY_USER_TOKEN, null);
				if(TextUtils.isEmpty(userToken)){
                    IntentManager.startLoginActivity(SplashActivity.this);
				}else{
                    IntentManager.startMainActivity(SplashActivity.this);
				}
			}
	        finish();
		}
    	
    };
}
