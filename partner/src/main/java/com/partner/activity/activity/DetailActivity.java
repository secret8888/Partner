package com.partner.activity.activity;

import android.os.Bundle;
import android.view.View;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.util.IntentManager;
import com.partner.view.TitleView;

public class DetailActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	private static final String TAG = DetailActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_detail;
	}

	@Override
	protected void readIntent() {
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.activity_detail);
		titleView.setOperate(R.drawable.ic_share);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	@Override
	public void onTitleOperateClick() {
		super.onTitleOperateClick();
	}

	public void onInstitutionClick(View view) {
		IntentManager.startInstitutionInfoActivity(this);
	}

	public void onSignedClick(View view) {
		IntentManager.startSignedUserActivity(this);
	}
}