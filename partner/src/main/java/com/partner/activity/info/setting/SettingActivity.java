package com.partner.activity.info.setting;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.util.IntentManager;
import com.partner.view.TitleView;

public class SettingActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.im_message_switch)
	private ImageView messageSwitchView;

	private static final String TAG = SettingActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_setting;
	}

	@Override
	protected void readIntent() {
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.setting);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	public void onSwitchClick() {
		messageSwitchView.setBackgroundResource(R.drawable.pref_check_box_off);
	}

	public void onFeedbackClick(View view) {
		IntentManager.startFeedbackActivity(this);
	}

	public void onAboutClick(View view) {
	}

	public void onModifyPsdClick(View view) {
	}

	public void onModifyPhoneClick(View view) {
	}

	public void onCheckUpdateClick(View view) {
	}
}