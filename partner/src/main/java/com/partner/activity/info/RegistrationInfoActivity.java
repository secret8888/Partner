package com.partner.activity.info;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.adapter.FriendAdapter;
import com.partner.adapter.RegistrationInfoAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.common.util.IntentManager;
import com.partner.common.util.Utils;
import com.partner.view.TitleView;
import com.partner.view.refresh.RefreshListView;

import java.util.ArrayList;

public class RegistrationInfoActivity extends BaseActivity implements AdapterView.OnItemClickListener {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.list_content)
	private ListView contentView;

	private static final String TAG = RegistrationInfoActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_registration_info;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.registration_info);
		titleView.setOperate(R.drawable.ic_add);

		ArrayList list = new ArrayList();
		for(int i = 0; i < 100; i ++) {
			list.add("");
		}
		RegistrationInfoAdapter adapter = new RegistrationInfoAdapter(this, list);
		contentView.setAdapter(adapter);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
		contentView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		IntentManager.startRegistrationEditActivity(this, false);
	}

	public void onMessageClick(View view) {
		onBackPressed();
	}

	@Override
	public void onTitleOperateClick() {
		super.onTitleOperateClick();
		IntentManager.startRegistrationEditActivity(this, true);
	}
}