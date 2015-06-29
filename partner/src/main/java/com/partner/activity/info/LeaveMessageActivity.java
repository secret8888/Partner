package com.partner.activity.info;

import android.os.Bundle;
import android.view.View;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.view.TitleView;

public class LeaveMessageActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	private static final String TAG = LeaveMessageActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_leave_message;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.leave_message);
		titleView.setOperateText(R.string.send);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	public void onMessageClick(View view) {
		onBackPressed();
	}

	@Override
	public void onTitleOperateClick() {
		super.onTitleOperateClick();
		onBackPressed();
	}
}