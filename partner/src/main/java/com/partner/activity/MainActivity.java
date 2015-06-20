package com.partner.activity;

import com.facebook.drawee.view.SimpleDraweeView;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;

import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends BaseActivity {

	@ViewId(R.id.image_view)
	private SimpleDraweeView draweeView;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		Uri uri = Uri.parse("https://raw.githubusercontent.com/liaohuqiu/fresco-docs-cn/docs/static/fresco-logo.png");
		draweeView.setImageURI(uri);
	}

	@Override
	protected void setListeners() {

	}
}
