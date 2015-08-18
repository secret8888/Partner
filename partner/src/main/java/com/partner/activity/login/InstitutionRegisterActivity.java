package com.partner.activity.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.IntentManager;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.view.TitleView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class InstitutionRegisterActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.edit_phone)
	private EditText phoneEdit;

	@ViewId(R.id.edit_psd)
	private EditText psdEdit;

	@ViewId(R.id.edit_code)
	private EditText codeEdit;

	@ViewId(R.id.edit_institution_name)
	private EditText institutionNameEdit;

	@ViewId(R.id.edit_institution_address)
	private EditText institutionAddressEdit;

	@ViewId(R.id.edit_institution_intro)
	private EditText institutionIntroEdit;

	@ViewId(R.id.tv_get_code)
	private TextView codeView;

	@ViewId(R.id.cbox_law)
	private CheckBox lawBox;

	private Timer codeTimer = null;

	private int timeLen = 60;

	private static final String TAG = InstitutionRegisterActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_institution_register;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.institution_register_title);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	public void onCodeClick(View view) {
		if (Utils.checkNetworkConnected(this)) {
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
		if (Utils.checkNetworkConnected(this)) {
			String phone = phoneEdit.getText().toString();
			String psd = psdEdit.getText().toString();
			String code = codeEdit.getText().toString();
			String institutionName = institutionNameEdit.getText().toString();
			String institutionAddress = institutionAddressEdit.getText().toString();
			String institutionIntro = institutionIntroEdit.getText().toString();

			if (TextUtils.isEmpty(institutionName)) {
				Toaster.show(this, R.string.input_institution_tip);
				return;
			}

			if (TextUtils.isEmpty(institutionAddress)) {
				Toaster.show(this, R.string.input_address_tip);
				return;
			}

			if (TextUtils.isEmpty(institutionIntro)) {
				Toaster.show(this, R.string.input_intro_tip);
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
			HttpManager.register(phone, psd, code, 1, institutionName, institutionAddress, institutionIntro, new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					onDismissLoadingDialog();
					if(TextUtils.isEmpty(HttpUtils.getResponseData(response))) {
						return;
					}
					Toaster.show(R.string.register_success);
					onBackPressed();
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
				}
			});
		}
	}

	public void onLawClick(View view) {
		IntentManager.startLawActivity(this);
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