package com.partner.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.PreferenceConsts;
import com.partner.common.util.IntentManager;
import com.partner.common.util.PreferenceUtils;

public class GuideActivity extends BaseActivity implements OnPageChangeListener{
    
	@ViewId(R.id.pager_guide)
	private ViewPager viewPager = null;
	
	private int scrollState;
	
	private boolean isStartActivity = false;
	
    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void readIntent() {

    }

    @Override
    protected void initControls(Bundle savedInstanceState) {
    	PreferenceUtils.putBoolean(PreferenceConsts.KEY_FIRST_LOGIN, false);
    	viewPager.setOffscreenPageLimit(viewPager.getChildCount());
    	viewPager.setAdapter(new GuidePagerAdapter());
    }

    @Override
    protected void setListeners() {
    	viewPager.setOnPageChangeListener(this);
    }

	@Override
	public void onPageScrollStateChanged(int scrollState) {
		this.scrollState = scrollState;
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if(!isStartActivity && scrollState == 1 && arg0 == 3){
			isStartActivity = true;
			IntentManager.startMainActivity(this);
	        finish();
		}
	}

	@Override
	public void onPageSelected(int arg0) {
	}
	
	class GuidePagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(View collection, int position) {
            return viewPager.getChildAt(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public int getCount() {
            return viewPager.getChildCount();
        }
    }

}
