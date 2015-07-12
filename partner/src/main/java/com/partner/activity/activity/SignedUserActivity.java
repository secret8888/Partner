package com.partner.activity.activity;

import android.os.Bundle;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.adapter.SignedUserAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.view.TitleView;
import com.partner.view.refresh.RefreshListView;

public class SignedUserActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.list_content)
	private RefreshListView contentView;

	private SignedUserAdapter adapter;

	private static final String TAG = SignedUserActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_signed_user;
	}

	@Override
	protected void readIntent() {
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.signed_user);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}
}