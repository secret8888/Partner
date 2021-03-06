package com.partner.activity.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.IntentConsts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.view.TitleView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ModifyPsdActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.lv_phone)
	private LinearLayout phoneLayout;

	@ViewId(R.id.edit_phone)
	private EditText phoneEdit;

	@ViewId(R.id.edit_psd)
	private EditText psdEdit;

	@ViewId(R.id.edit_code)
	private EditText codeEdit;

	@ViewId(R.id.tv_get_code)
	private TextView codeView;

	private boolean isSettingModify = false;

	private Timer codeTimer = null;

	private int timeLen = 60;

	private static final String TAG = ModifyPsdActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_modify_psd;
	}

	@Override
	protected void readIntent() {
		isSettingModify = getIntent().getBooleanExtra(IntentConsts.SETTING_MODIFY_KEY, false);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		if(isSettingModify) {
			titleView.setTitle(R.string.change_psd);
			phoneLayout.setVisibility(View.GONE);
		} else {
			titleView.setTitle(R.string.modify_psd);
		}
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	public void onCodeClick(View view) {
		if (Utils.checkNetworkConnected(this)) {
			String phone = phoneEdit.getText().toString();

			if(isSettingModify) {
				phone = PartnerApplication.getInstance().getUserInfo().getCellphone();
			} else if (TextUtils.isEmpty(phone)) {
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

			if(isSettingModify) {
				phone = PartnerApplication.getInstance().getUserInfo().getCellphone();
			} else if (TextUtils.isEmpty(phone)) {
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

			onShowLoadingDialog();
			HttpManager.resetPassword(phone, psd, code, 0, new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					onDismissLoadingDialog();
					onBackPressed();
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
				}
			});
		}
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
						if(timeLen < 1){
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