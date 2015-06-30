package com.partner.activity.info;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.IntentConsts;
import com.partner.view.TitleView;

public class MyQrCodeActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.im_avatar)
	private SimpleDraweeView avatarView;

	@ViewId(R.id.tv_name)
	private TextView nameView;

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
		nameView.setText(PartnerApplication.getInstance().getUserInfo().getUserName());
		String avatarImage = PartnerApplication.getInstance().getUserInfo().getHeadImage();
		if(!TextUtils.isEmpty(avatarImage)) {
			Uri uri = Uri.parse(avatarImage);
			avatarView.setImageURI(uri);
		}
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}
}