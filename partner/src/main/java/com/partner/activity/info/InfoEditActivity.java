package com.partner.activity.info;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.util.IntentManager;
import com.partner.view.TitleView;

public class InfoEditActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	private static final String TAG = InfoEditActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_info_edit;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.app_name);
		titleView.setOperateText(R.string.delete);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	public void onMessageClick(View view) {
		IntentManager.startLeaveMessageActivity(this);
	}

	@Override
	public void onTitleOperateClick() {
		super.onTitleOperateClick();
		onBackPressed();
	}
}