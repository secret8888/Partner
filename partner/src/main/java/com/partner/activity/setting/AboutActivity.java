package com.partner.activity.setting;

import android.os.Bundle;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.view.TitleView;

public class AboutActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	private static final String TAG = AboutActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_about;
	}

	@Override
	protected void readIntent() {}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.about);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

}