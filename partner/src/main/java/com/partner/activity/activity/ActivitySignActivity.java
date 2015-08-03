package com.partner.activity.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.adapter.RegistrationInfoAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.IntentConsts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.IntentManager;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.model.RegistrationInfo;
import com.partner.model.RegistrationList;
import com.partner.view.TitleView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.youdao.yjson.YJson;

import java.io.IOException;

public class ActivitySignActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.edit_signed_num)
	private EditText signedNumView;

	@ViewId(R.id.list_content)
	private ListView contentView;

	private int activityId;

	private RegistrationList registrationList;

	private StringBuilder inrollIdsBuilder;

	private static final String TAG = ActivitySignActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_sign;
	}

	@Override
	protected void readIntent() {
		activityId = getIntent().getIntExtra(IntentConsts.ID_KEY, -1);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.sign);
		titleView.setOperateText(R.string.add);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getRegistList();
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	public void onFinishClick(View view) {
		signActivity();
	}

	public void onAddNumChanged() {
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
		RegistrationInfoAdapter adapter = new RegistrationInfoAdapter(this, registrationList.getInrollInfos(), true);
		contentView.setAdapter(adapter);
	}

	private void signActivity() {
		int signNum = Integer.parseInt(signedNumView.getText().toString());
		if(signNum <= 0) {
			Toaster.show(R.string.sign_select);
			return;
		}
		inrollIdsBuilder = new StringBuilder();
		for(RegistrationInfo info : registrationList.getInrollInfos()) {
			if(info.isRegistrationAdd()) {
				inrollIdsBuilder.append(info.getUserenrollInfoId());
				inrollIdsBuilder.append(",");
			}
		}
		String inrollIds = inrollIdsBuilder.toString();
		if(inrollIds.length() < 1) {
			Toaster.show(R.string.parent_select);
			return;
		}
		if (Utils.checkNetworkConnected(this)) {
			onShowLoadingDialog();
			HttpManager.signActivity(PartnerApplication.getInstance().getUserInfo().getToken(), activityId,
					signNum, inrollIds, new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					Toaster.show(R.string.activity_sign_success);
					onBackPressed();
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
				}
			});
		}
	}
}