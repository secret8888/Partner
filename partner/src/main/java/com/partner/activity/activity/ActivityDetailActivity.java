package com.partner.activity.activity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.partner.common.util.ShareSDKManager;
import com.partner.common.util.Toaster;
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

	@ViewId(R.id.tv_phone)
	private TextView phoneView;

	@ViewId(R.id.tv_desc)
	private TextView descView;

	@ViewId(R.id.tv_start_time)
	private TextView startTimeView;

	@ViewId(R.id.tv_end_time)
	private TextView endTimeView;

	@ViewId(R.id.tv_activity_address)
	private TextView activityAddressView;

	@ViewId(R.id.tv_activity_num)
	private TextView activityNumView;

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
		ShareSDKManager.getInstance(this).shareWebPage("title", "desc", "http://img1.cache.netease.com/catchpic/9/90/90F93C644F394EA8D7539EF4BA6DE4FE.jpg", "http://www.baidu.com");
	}

	private void initActivityInfo() {
		activityTitleView.setText(mInfo.getActivityTitle());
		signNumView.setText(String.format(getString(R.string.sign_num), mInfo.getActivityAgainNum()));
		viewNumView.setText(String.format(getString(R.string.view_num), mInfo.getActivityViewNum()));
		locationView.setText(mInfo.getActivityAddress());
		phoneView.setText(mInfo.getActivityLinkmanPhone());
		descView.setText(mInfo.getActivityDescription());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		startTimeView.setText(format.format(new Date(mInfo.getCreateTime() * 1000)));
		endTimeView.setText(format.format(new Date(mInfo.getCreateTime() * 1000)));
		activityAddressView.setText(mInfo.getActivityAddress());
		activityNumView.setText(String.format(getString(R.string.people_num), mInfo.getActivityPeapleNum()));

		if(!TextUtils.isEmpty(mInfo.getActivityImage())) {
			introView.setImageURI(Uri.parse(mInfo.getActivityImage()));
		}
	}

	public void onFollowClick(View view) {
		if(Utils.isNetworkConnected(this)) {
			onShowLoadingDialog();
			HttpManager.followActivity(PartnerApplication.getInstance().getUserInfo().getToken(), activityId, new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					super.onRequestResponse(response);
					onDismissLoadingDialog();
					String result = HttpUtils.getResponseData(response);
					if (!TextUtils.isEmpty(result)) {
						Toaster.show(R.string.follow_success);
					}
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					super.onRequestFailure(request, e);
					Toaster.show(R.string.follow_fail);
					onDismissLoadingDialog();
				}
			});
		}
	}

	public void onInstitutionClick(View view) {
		IntentManager.startInstitutionInfoActivity(this, mInfo.getActivityUserId());
	}

	public void onDetailClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.activity_detail_intro),
				mInfo.getActivityDescription());
	}

	public void onPathClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.path_intro),
				mInfo.getActivityTransportInfo());
	}

	public void onCostClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.activity_cost),
				mInfo.getActivityCost() + " 元");
	}

	public void onTravelClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.travel_assign),
				mInfo.getActivityDescription());
	}

	public void onEquipClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.equipment_require),
				mInfo.getActivityDescription());
	}

	public void onSignedClick(View view) {
		IntentManager.startSignedUserActivity(this, activityId);
	}

	public void onSignClick(View view) {
		IntentManager.startActivitySignActivity(ActivityDetailActivity.this, activityId);
	}

	public void onInviteClick(View view) {
		IntentManager.startInviteActivity(ActivityDetailActivity.this);
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