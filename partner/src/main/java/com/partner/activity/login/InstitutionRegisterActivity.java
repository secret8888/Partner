package com.partner.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.view.TitleView;

public class InstitutionRegisterActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.edit_phone)
	private EditText phoneEdit;

	@ViewId(R.id.edit_psd)
	private EditText psdEdit;

	@ViewId(R.id.edit_code)
	private EditText codeEdit;

	@ViewId(R.id.tv_get_code)
	private TextView codeView;

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
		titleView.setTitle(R.string.modify_psd);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	public void onCodeClick(View view) {
//		if (Utils.checkMetworkConnected(this)) {
//			String phone = phoneEdit.getText().toString();
//			String psd = psdEdit.getText().toString();
//
//			if (TextUtils.isEmpty(phone)) {
//				MyToast.show(this, R.string.phone_not_null);
//				return;
//			}
//
//			if (TextUtils.isEmpty(psd)) {
//				MyToast.show(this, R.string.psd_not_null);
//				return;
//			}
//			showRefreshingView();
//			HttpManager.login(new CoinvsHandler(this) {
//
//				@Override
//				protected void handleSuccessMessage(Object object) {
//					handleLoginResult((HandleInfo) object);
//				}
//
//				@Override
//				protected void handleError(int errorCode) {
//					hideRefreshingView();
//				}
//			}, phone, psd);
//		}
	}

	public void onConfirmClick(View view) {

	}

//	private void handleLoginResult(HandleInfo handleInfo) {
//		hideRefreshingView();
//		if(handleInfo == null || handleInfo.getData() == null) {
//			return;
//		}
//		Consts.IS_MINE_NEED_REFRESH = true;
//		try {
//			JSONObject dataObject = new JSONObject(handleInfo.getData());
//			PreferenceUtils.putString(PreferenceConsts.KEY_LOGIN_ID,
//					dataObject.optString("login_id"));
//			PreferenceUtils.putString(PreferenceConsts.KEY_LOGIN_NAME,
//					dataObject.optString("login_name"));
//			PreferenceUtils.putString(PreferenceConsts.KEY_USER_PHONE,
//					dataObject.optString("login_phone"));
//			ActivityUtils.startMainActivity(this);
//			finish();
//		} catch (JSONException e) {
//			Logcat.e(TAG, "handleLoginResult Json error", e);
//		}
//	}
}