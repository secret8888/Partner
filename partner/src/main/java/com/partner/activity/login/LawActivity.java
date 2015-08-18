package com.partner.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.IntentConsts;
import com.partner.common.util.Utils;
import com.partner.view.TitleView;

public class LawActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.tv_content)
	private TextView contentView;

	private String content;

	private static final String TAG = LawActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_law;
	}

	@Override
	protected void readIntent() {
		content = getIntent().getStringExtra(IntentConsts.INFO_KEY);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.law_article);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}
}