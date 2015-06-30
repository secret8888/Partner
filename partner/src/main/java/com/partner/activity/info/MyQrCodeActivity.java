package com.partner.activity.info;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.IntentConsts;
import com.partner.view.TitleView;

public class MyQrCodeActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	private static final String TAG = MyQrCodeActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_my_qrcode;
	}

	@Override
	protected void readIntent() {
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.my_qrcode);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}
}