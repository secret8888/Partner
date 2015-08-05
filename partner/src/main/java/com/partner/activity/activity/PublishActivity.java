package com.partner.activity.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.IntentConsts;
import com.partner.common.util.IntentManager;
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

	public void onDetailClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.activity_detail_intro), null, true);
	}

	public void onPathClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.path_intro), null, true);
	}

	public void onCostClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.activity_cost), null, true);
	}

	public void onTravelClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.travel_assign), null, true);
	}

	public void onEquipClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.equipment_require), null, true);
	}
}