package com.partner.activity.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.PreferenceConsts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.IntentManager;
import com.partner.common.util.Logcat;
import com.partner.common.util.PreferenceUtils;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.model.UserInfo;
import com.partner.view.TitleView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.youdao.yjson.YJson;

import java.io.IOException;

public class LoginActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.edit_phone)
	private EditText phoneEdit;

	@ViewId(R.id.edit_psd)
	private EditText psdEdit;

	private static final String TAG = LoginActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_login;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.login);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	public void onLoginClick(View view) {
		if (Utils.checkNetworkConnected(this)) {
			String phone = phoneEdit.getText().toString();
			String psd = psdEdit.getText().toString();

			if (TextUtils.isEmpty(phone)) {
				Toaster.show(this, R.string.phone_not_null);
				return;
			}

			if (TextUtils.isEmpty(psd)) {
				Toaster.show(this, R.string.psd_not_null);
				return;
			}
			onShowLoadingDialog();
			HttpManager.login(phone, psd, new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					handleLoginResult(response);
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
				}
			});
		}
	}

	public void onFindPsdClick(View view) {
		IntentManager.startModifyPsdActivity(this, false);
	}

	public void onRegisterClick(View view) {
		IntentManager.startRegisterActivity(this);
	}

	private void handleLoginResult(Response response) {
		onDismissLoadingDialog();
		String result = HttpUtils.getResponseData(response);
		if(!TextUtils.isEmpty(result)) {
			PreferenceUtils.putString(PreferenceConsts.KEY_USER_INFO, result);
			IntentManager.startMainActivity(this);
			finish();
		}
	}
}