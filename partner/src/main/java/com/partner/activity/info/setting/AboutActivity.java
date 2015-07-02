package com.partner.activity.info.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
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

public class AboutActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	private static final String TAG = AboutActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_about;
	}

	@Override
	protected void readIntent() {}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.about);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

}