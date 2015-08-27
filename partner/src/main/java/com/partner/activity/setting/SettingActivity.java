package com.partner.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.IntentConsts;
import com.partner.common.constant.PreferenceConsts;
import com.partner.common.util.IntentManager;
import com.partner.common.util.PreferenceUtils;
import com.partner.common.util.Toaster;
import com.partner.view.TitleView;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

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

	public void onSwitchClick(View view) {
		boolean isMessagePush = PreferenceUtils.getBoolean(PreferenceConsts.KEY_MESSAGE_PUSH, true);
		if(isMessagePush) {
			PreferenceUtils.putBoolean(PreferenceConsts.KEY_MESSAGE_PUSH, false);
			messageSwitchView.setBackgroundResource(R.drawable.pref_check_box_off);
		} else {
			PreferenceUtils.putBoolean(PreferenceConsts.KEY_MESSAGE_PUSH, true);
			messageSwitchView.setBackgroundResource(R.drawable.pref_check_box_on);
		}
	}

	public void onFeedbackClick(View view) {
		IntentManager.startFeedbackActivity(this);
	}

	public void onAboutClick(View view) {
		IntentManager.startAboutActivity(this);
	}

	public void onModifyPsdClick(View view) {
		IntentManager.startModifyPsdActivity(this, true);
	}

	public void onModifyPhoneClick(View view) {
		IntentManager.startModifyPhoneActivity(this);
	}

	public void onCheckUpdateClick(View view) {
		onShowLoadingDialog();
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
				onDismissLoadingDialog();
				switch (updateStatus) {
					case UpdateStatus.Yes: // has update
						UmengUpdateAgent.showUpdateDialog(SettingActivity.this, updateInfo);
						break;
					case UpdateStatus.No: // has no update
						Toaster.showMsg(SettingActivity.this, "已是最新版本");
						break;
				}
			}
		});
		UmengUpdateAgent.update(this);
	}

	public void onLogoutClick(View view) {
		PreferenceUtils.remove(PreferenceConsts.KEY_USER_INFO);
		PartnerApplication.getInstance().cleanUserInfo();
		PartnerApplication.getInstance().setLogin(false);
		Intent intent = new Intent();
		intent.putExtra(IntentConsts.LOGOUT_KEY, true);
		setResult(RESULT_OK, intent);
		onBackPressed();
	}
}