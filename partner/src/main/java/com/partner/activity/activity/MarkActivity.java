package com.partner.activity.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.view.TitleView;

import java.util.Hashtable;

public class MarkActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	private static final String TAG = MarkActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_mark;
	}

	@Override
	protected void readIntent() {
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.activity_mark);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}
}