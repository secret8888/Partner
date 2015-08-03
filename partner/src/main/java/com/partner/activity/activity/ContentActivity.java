package com.partner.activity.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.IntentConsts;
import com.partner.view.TitleView;

public class ContentActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.tv_content)
	private TextView contentView;

	private String title;

	private String content;

	private static final String TAG = ContentActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_content;
	}

	@Override
	protected void readIntent() {
		title = getIntent().getStringExtra(IntentConsts.TITLE_KEY);
		content = getIntent().getStringExtra(IntentConsts.INFO_KEY);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(title);
		contentView.setText(content);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}
}