package com.partner.activity.info;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.adapter.FriendAdapter;
import com.partner.adapter.RegistrationInfoAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.IntentManager;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.model.RegistrationInfo;
import com.partner.model.RegistrationList;
import com.partner.view.TitleView;
import com.partner.view.refresh.RefreshListView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.youdao.yjson.YJson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class RegistrationInfoActivity extends BaseActivity implements AdapterView.OnItemClickListener {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.list_content)
	private ListView contentView;

	private RegistrationList registrationList;

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
	}

	@Override
	protected void onResume() {
		super.onResume();
		getRegistList();
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
		contentView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		IntentManager.startRegistrationEditActivity(this, registrationList.getInrollInfos().get(position));
	}

	public void onMessageClick(View view) {
		onBackPressed();
	}

	@Override
	public void onTitleOperateClick() {
		super.onTitleOperateClick();
		IntentManager.startRegistrationEditActivity(this, null);
	}

	private void getRegistList() {
		if (Utils.checkNetworkConnected(this)) {
			onShowLoadingDialog();
			HttpManager.getAllRegistList(PartnerApplication.getInstance().getUserInfo().getToken(), new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					handleRegitsResult(response);
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
				}
			});
		}
	}

	private void handleRegitsResult(Response response) {
		onDismissLoadingDialog();
		String result = HttpUtils.getResponseData(response);

		registrationList = YJson.getObj(result, RegistrationList.class);
		RegistrationInfoAdapter adapter = new RegistrationInfoAdapter(this, registrationList.getInrollInfos());
		contentView.setAdapter(adapter);
	}
}