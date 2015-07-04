package com.partner.activity.info.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.util.IntentManager;
import com.partner.common.util.Toaster;
import com.partner.view.TitleView;

public class ModifyPhoneActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.tv_phone)
	private TextView phoneView;

	@ViewId(R.id.edit_phone)
	private EditText phoneText;

	private static final String TAG = ModifyPhoneActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_modify_phone;
	}

	@Override
	protected void readIntent() {
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.modify_phone);
		titleView.setOperateText(R.string.next);

		phoneView.setText(String.format(getString(R.string.phone_tip),
				PartnerApplication.getInstance().getUserInfo().getCellphone()));
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	@Override
	public void onTitleOperateClick() {
		super.onTitleOperateClick();
		String phone = phoneText.getText().toString();
		if(TextUtils.isEmpty(phone)) {
			Toaster.show(R.string.input_phone_tip);
			return;
		}
		IntentManager.startModifyPhoneNextActivity(this, phone, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0 && resultCode == RESULT_OK) {
			onBackPressed();
		}
	}
}