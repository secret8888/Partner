package com.partner.activity.login;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.PreferenceConsts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.IntentManager;
import com.partner.common.util.PreferenceUtils;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.view.TitleView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


public class RegisterActivity extends BaseActivity {
	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.edit_nickname)
	private EditText nicknameEdit;

	@ViewId(R.id.edit_phone)
	private EditText phoneEdit;

	@ViewId(R.id.edit_psd)
	private EditText psdEdit;

	@ViewId(R.id.edit_code)
	private EditText codeEdit;

	@ViewId(R.id.tv_get_code)
	private TextView codeView;

	@ViewId(R.id.tv_institution_register)
	private TextView institutionRegisterView;

	@ViewId(R.id.cbox_law)
	private CheckBox lawBox;

	private Timer codeTimer = null;

	private int timeLen = 60;

	private static final String TAG = ModifyPsdActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_register;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.user_register);
		institutionRegisterView.getPaint().setUnderlineText(true);
		institutionRegisterView.setText(R.string.institution_register);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	public void onCodeClick(View view) {
		if (Utils.checkMetworkConnected(this)) {
			String phone = phoneEdit.getText().toString();

			if (TextUtils.isEmpty(phone)) {
				Toaster.show(this, R.string.phone_not_null);
				return;
			}

			onShowLoadingDialog();
			HttpManager.getCode(phone, new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					handleCodeResult(response);
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
				}
			});
		}
	}

	public void onConfirmClick(View view) {
		if (Utils.checkMetworkConnected(this)) {
			String nickname = nicknameEdit.getText().toString();
			String phone = phoneEdit.getText().toString();
			String psd = psdEdit.getText().toString();
			String code = codeEdit.getText().toString();

			if (TextUtils.isEmpty(nickname)) {
				Toaster.show(this, R.string.input_nickname_tip);
				return;
			}

			if (TextUtils.isEmpty(phone)) {
				Toaster.show(this, R.string.phone_not_null);
				return;
			}

			if (TextUtils.isEmpty(psd)) {
				Toaster.show(this, R.string.psd_not_null);
				return;
			}

			if (TextUtils.isEmpty(code)) {
				Toaster.show(this, R.string.input_code_tip);
				return;
			}

			if(!lawBox.isChecked()) {
				Toaster.show(this, R.string.check_law_tip);
				return;
			}

			onShowLoadingDialog();
			HttpManager.register(phone, psd, code, 0, nickname, null, null, new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					handleRegisterResult(response);
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
				}
			});
		}
	}

	public void onInstitutionRegisterClick(View view) {
		IntentManager.startInstitutionRegisterActivity(this);
		finish();
	}

	private void handleRegisterResult(Response response) {
		onDismissLoadingDialog();
		if(TextUtils.isEmpty(HttpUtils.getResponseData(response))) {
			return;
		}
		Toaster.show(R.string.register_success);
		onBackPressed();
	}

	private void handleCodeResult(Response response) {
		onDismissLoadingDialog();
		HttpUtils.getResponseData(response);
		Toaster.show(R.string.send_success);
		codeView.setEnabled(false);
		codeView.setBackgroundResource(R.drawable.btn_disable);
		codeTimer = new Timer();
		codeTimer.schedule(new TimerTask() {
			@Override
			public void run() {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						timeLen--;
						codeView.setText(getResources().getString(R.string.get_code) + "(" + timeLen + ")");
						if (timeLen < 1) {
							timeLen = 60;
							codeTimer.cancel();
							codeView.setEnabled(true);
							codeView.setBackgroundResource(R.drawable.btn_operate);
							codeView.setText(R.string.get_code);
						}
					}
				});
			}
		}, 1000, 1000);
	}
}