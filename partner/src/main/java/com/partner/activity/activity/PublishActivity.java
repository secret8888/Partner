package com.partner.activity.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.IntentConsts;
import com.partner.view.TitleView;

public class PublishActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

//	@ViewId(R.id.tv_content)
//	private TextView contentView;

	private static final String TAG = PublishActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_publish;
	}

	@Override
	protected void readIntent() {
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.activity);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}
}