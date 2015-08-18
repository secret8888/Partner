package com.partner.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseFragmentActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.Consts;
import com.partner.common.constant.IntentConsts;
import com.partner.common.constant.PreferenceConsts;
import com.partner.common.util.IntentManager;
import com.partner.common.util.PreferenceUtils;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.fragment.FriendFragment;
import com.partner.fragment.MainFragment;
import com.partner.fragment.MineFragment;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends BaseFragmentActivity {

	// 定义FragmentTabHost对象
	private FragmentTabHost mTabHost;

	@ViewId(R.id.tab_rg_menu)
	private RadioGroup mTabRg;

	@ViewId(R.id.tab_activity)
	private RadioButton activityRadio;

	@ViewId(R.id.tab_mine)
	private RadioButton mineRadio;

	@ViewId(R.id.im_badge)
	private ImageView badgeView;

	private boolean isFriendNeedRefresh = false;
	// exit time
	private long mExitTime;

	private boolean isBusiness;

	private final Class<?>[] fragments = { MainFragment.class, FriendFragment.class, MineFragment.class };

	@Override
	protected int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		PartnerApplication.getInstance().initUserInfo();
		isBusiness = PartnerApplication.getInstance().getUserInfo().getUserType() == Consts.ROLE_BUSINESS;
		initView();
		UmengUpdateAgent.update(this);
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY,
				Utils.getMetaValue(MainActivity.this, "api_key"));
		showBadge();
	}

	@Override
	protected void setListeners() {

	}

	private void initView() {
		if(isBusiness) {
			activityRadio.setText(R.string.fans);
			mineRadio.setText(R.string.mine);
		}
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		// 得到fragment的个数
		int count = fragments.length;
		for (int i = 0; i < count; i++) {
			// 为每一个Tab按钮设置图标、文字和内容
			TabHost.TabSpec tabSpec = mTabHost.newTabSpec(i + "").setIndicator(i + "");
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragments[i], null);
		}

		mTabRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.tab_main:
						mTabHost.setCurrentTab(0);
						break;
					case R.id.tab_activity:
						mTabHost.setCurrentTab(1);
						break;
					case R.id.tab_mine:
						mTabHost.setCurrentTab(2);
						if (badgeView.getVisibility() == View.VISIBLE) {
							badgeView.setVisibility(View.GONE);
						}
						break;
				}
			}
		});
	}

	private void showBadge() {
		int num = PreferenceUtils.getInt(PreferenceConsts.KEY_MESSAGE_NUM, 0);
		if(num > 0) {
			badgeView.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)badgeView.getLayoutParams();
			params.rightMargin=Utils.getScreenWidth(this)/6 - 70;
			badgeView.setLayoutParams(params);
		} else {
			badgeView.setVisibility(View.GONE);
		}
	}

	public boolean isFriendNeedRefresh() {
		return isFriendNeedRefresh;
	}

	public void setIsFriendNeedRefresh(boolean isFriendNeedRefresh) {
		this.isFriendNeedRefresh = isFriendNeedRefresh;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0 && resultCode == RESULT_OK) {
			boolean isLogout = data.getBooleanExtra(IntentConsts.LOGOUT_KEY, false);
			if(isLogout) {
				IntentManager.startLoginActivity(this);
				finish();
			}
		} else if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
			setIsFriendNeedRefresh(true);
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// if(onFragmentBackListener != null &&
			// onFragmentBackListener.onFragmentBackClick()){
			// return true;
			// }
			return exitApplication();
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * 判断两次返回时间间隔,小于两秒则退出程序
	 */
	private boolean exitApplication() {
		if (System.currentTimeMillis() - mExitTime > Consts.INTERVAL) {
			Toaster.show(this, R.string.exit_tip);
			mExitTime = System.currentTimeMillis();
			return true;
		} else {
			finish();
			return false;
		}
	}
}
