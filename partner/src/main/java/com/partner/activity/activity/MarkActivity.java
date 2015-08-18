package com.partner.activity.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.partner.common.constant.IntentConsts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.model.MessageInfo;
import com.partner.view.TitleView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

public class MarkActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.tv_activity_title)
	private TextView activityTitleView;

	@ViewId(R.id.tv_time)
	private TextView timeView;

	@ViewId(R.id.tv_institution)
	private TextView institutionView;

	@ViewId(R.id.rbar_comment)
	private RatingBar commentBar;

	private MessageInfo mInfo;

	private static final String TAG = MarkActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_mark;
	}

	@Override
	protected void readIntent() {
		mInfo = (MessageInfo)getIntent().getSerializableExtra(IntentConsts.INFO_KEY);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.activity_mark);
		activityTitleView.setText(String.format(getString(R.string.activity_title), mInfo.getActivityTitle()));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd : hh:mm");
		timeView.setText(String.format(getString(R.string.activity_time), format.format(new Date(mInfo.getActivityStartTime()))));
		institutionView.setText(String.format(getString(R.string.activity_instrument), mInfo.getMessageFromUserName()));
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	public void onCommitClick(View view) {
		if (Utils.checkNetworkConnected(this)) {
			onShowLoadingDialog();
			HttpManager.commentActivity(PartnerApplication.getInstance().getUserInfo().getToken(), mInfo.getMessageActivity(),
					(int)commentBar.getRating(), new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					onDismissLoadingDialog();
					String responseBody = HttpUtils.getResponseData(response);
					if(!TextUtils.isEmpty(responseBody)) {
						Toaster.show(R.string.operate_success);
						onBackPressed();
					}
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
				}
			});
		}
	}
}