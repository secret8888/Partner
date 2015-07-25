package com.partner.activity.activity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.IntentConsts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.IntentManager;
import com.partner.common.util.Utils;
import com.partner.model.ActivityInfo;
import com.partner.view.TitleView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.youdao.yjson.YJson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityDetailActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.im_activity_intro)
	private SimpleDraweeView introView;

	@ViewId(R.id.tv_activity_title)
	private TextView activityTitleView;

	@ViewId(R.id.tv_sign_num)
	private TextView signNumView;

	@ViewId(R.id.tv_view_num)
	private TextView viewNumView;

	@ViewId(R.id.tv_location)
	private TextView locationView;

	@ViewId(R.id.tv_publish_name)
	private TextView publishNameView;

	@ViewId(R.id.tv_phone)
	private TextView phoneView;

	@ViewId(R.id.tv_desc)
	private TextView descView;

	@ViewId(R.id.tv_gather_time)
	private TextView gatherTimeView;

	@ViewId(R.id.tv_gather_address)
	private TextView gatherAddressView;

	@ViewId(R.id.tv_start_address)
	private TextView startAddressView;

	@ViewId(R.id.tv_activity_address)
	private TextView activityAddressView;

	@ViewId(R.id.tv_charter_notice)
	private TextView charterNoticeView;

	@ViewId(R.id.tv_distance)
	private TextView distanceView;

	private ActivityInfo mInfo;

	private int activityId;

	private static final String TAG = ActivityDetailActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_detail;
	}

	@Override
	protected void readIntent() {
		activityId = getIntent().getIntExtra(IntentConsts.ID_KEY, -1);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.activity_detail);
		titleView.setOperate(R.drawable.ic_share);
		getActivityDetail();
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	@Override
	public void onTitleOperateClick() {
		super.onTitleOperateClick();
	}

	private void initActivityInfo() {
		activityTitleView.setText(mInfo.getActivityTitle());
		signNumView.setText(String.format(getString(R.string.sign_num), mInfo.getActivityPeapleNum()));
		viewNumView.setText(String.format(getString(R.string.view_num), mInfo.getActivityPeapleNum()));
		locationView.setText(mInfo.getActivityAddress());
		publishNameView.setText(String.format(getString(R.string.publish_name), mInfo.getActivityLinkmanName()));
		phoneView.setText(mInfo.getActivityLinkmanPhone());
		descView.setText(mInfo.getActivityDescription());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		gatherTimeView.setText(format.format(new Date(mInfo.getActivityGatherTime() * 1000)));
		gatherAddressView.setText(mInfo.getActivityAddress());
		startAddressView.setText(mInfo.getActivityAddress());
		activityAddressView.setText(mInfo.getActivityAddress());
		charterNoticeView.setText("================");
		distanceView.setText("===============");

		if(!TextUtils.isEmpty(mInfo.getActivityImage())) {
			introView.setImageURI(Uri.parse(mInfo.getActivityImage()));
		}
	}
	public void onInstitutionClick(View view) {
		IntentManager.startInstitutionInfoActivity(this);
	}

	public void onSignedClick(View view) {
		IntentManager.startSignedUserActivity(this, activityId);
	}

	public void onSignClick(View view) {
		IntentManager.startActivitySignActivity(ActivityDetailActivity.this);
	}

	public void onInviteClick(View view) {

	}

	private void getActivityDetail() {
		if(Utils.checkNetworkConnected(this)) {
			onShowLoadingDialog();
			HttpManager.getActivityDetail(PartnerApplication.getInstance().getUserInfo().getToken(), activityId, new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					handleActivityDetail(response);
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
				}
			});
		}
	}

	private void handleActivityDetail(Response response) {
		onDismissLoadingDialog();
		String responseBody = HttpUtils.getResponseData(response);
		mInfo = YJson.getObj(responseBody, ActivityInfo.class);
		initActivityInfo();
	}
}