package com.partner.activity.info;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.adapter.RegistrationInfoAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.IntentConsts;
import com.partner.view.TitleView;

import java.util.ArrayList;

public class RegistrationEditActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.tv_delete)
	private TextView deleteView;

	private boolean isParentAdd = false;

	private static final String TAG = RegistrationEditActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_registration_edit;
	}

	@Override
	protected void readIntent() {
		isParentAdd = getIntent().getBooleanExtra(IntentConsts.ITEM_EDIT_KEY, false);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		if(isParentAdd) {
			titleView.setTitle(R.string.parent_add);
		} else {
			titleView.setTitle(R.string.parent_info_edit);
			deleteView.setVisibility(View.VISIBLE);
		}
		titleView.setOperateText(R.string.finish);
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

	public void onDeleteClick(View view) {

	}
}