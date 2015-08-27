package com.partner.activity.activity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.Consts;
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

	@ViewId(R.id.lv_institution)
	private RelativeLayout institutionLayout;

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

	@ViewId(R.id.tab_rg_menu)
	private RadioGroup menuGroup;

	@ViewId(R.id.tab_follow)
	private RadioButton followButton;

	@ViewId(R.id.tab_sign)
	private RadioButton signButton;

	@ViewId(R.id.tab_invite)
	private RadioButton inviteButton;

	@ViewId(R.id.tab_share)
	private RadioButton shareButton;

	@ViewId(R.id.lv_again)
	private LinearLayout againLayout;

	@ViewId(R.id.tv_again_tip)
	private TextView againView;

	private ActivityInfo mInfo;

	private int activityId;

	private boolean isBusiness;

	private boolean isMine;

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
		isBusiness = PartnerApplication.getInstance().getUserInfo().getUserType() == Consts.ROLE_BUSINESS;
		titleView.setTitle(R.string.activity_detail);
		getActivityDetail();
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	@Override
	public void onTitleOperateClick() {
		super.onTitleOperateClick();
		if(isBusiness && isMine) {
			IntentManager.startPublishActivity(this, mInfo);
		} else {
			shareActivity();
		}
	}

	private void shareActivity() {
		ShareSDKManager.getInstance(this).shareWebPage(getString(R.string.activity_share_title), mInfo.getActivityTitle(),
				mInfo.getActivityImage(), String.format(getString(R.string.activity_share_url), mInfo.getActivityId()));
	}

	private void checkBusinessRole() {
		isMine = mInfo.getActivityCellphone().equals(PartnerApplication.getInstance().getUserInfo().getCellphone());
		if(isBusiness) {
			if(isMine) {
				titleView.setOperateText(R.string.edit);
				institutionLayout.setVisibility(View.GONE);
				followButton.setText(R.string.send_notice);
				followButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_send_notice, 0, 0);
				signButton.setText(R.string.message_record);
				signButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_message_record, 0, 0);
				shareButton.setVisibility(View.VISIBLE);
			} else {
				menuGroup.setVisibility(View.GONE);
				titleView.setOperate(R.drawable.ic_activity_share);
			}

		} else {
			titleView.setOperate(R.drawable.ic_activity_share);
		}
	}

	private void initActivityInfo() {
		activityTitleView.setText(mInfo.getActivityTitle());
		signNumView.setText(String.format(getString(R.string.sign_num), mInfo.getActivityPeapleNum()));
		viewNumView.setText(String.format(getString(R.string.view_num), mInfo.getActivityViewNum()));
		locationView.setText(mInfo.getActivityAddress());
		phoneView.setText(mInfo.getActivityCellphone());
		descView.setText(mInfo.getActivityDescription());
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		startTimeView.setText(format.format(new Date(mInfo.getActivityStartTime())));
		endTimeView.setText(format.format(new Date(mInfo.getActivityEndTime())));
		activityAddressView.setText(mInfo.getActivityAddress());
		activityNumView.setText(String.format(getString(R.string.people_num), mInfo.getActivityPeapleNum()));

		if(!TextUtils.isEmpty(mInfo.getActivityImage())) {
			introView.setImageURI(Uri.parse(mInfo.getActivityImage()));
		}

		if(mInfo.getActivityStatus() == 1) {
			againLayout.setVisibility(View.VISIBLE);
			againView.setVisibility(View.VISIBLE);
		}

		checkBusinessRole();
	}

	public void onFollowClick(View view) {
		if(isBusiness) {
			IntentManager.startLeaveMessageActivity(this, -1, activityId);
		} else {
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
	}

	public void onInstitutionClick(View view) {
		IntentManager.startInstitutionInfoActivity(this, mInfo.getActivityUserId());
	}

	public void onDetailClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.activity_detail_intro),
				mInfo.getActivityDescription(), false, -1, -1);
	}

	public void onPathClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.path_intro),
				mInfo.getActivityTransportInfo(), false, -1, -1);
	}

	public void onCostClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.activity_cost),
				mInfo.getActivityCost() + " 元", false, -1, -1);
	}

	public void onTravelClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.travel_assign),
				mInfo.getActivityArrange(), false, -1, -1);
	}

	public void onEquipClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.equipment_require),
				mInfo.getActivityEquipment(), false, -1, -1);
	}

	public void onAgainClick(View view) {
		wantFinishedActivity();
	}

	public void onSignedClick(View view) {
		IntentManager.startSignedUserActivity(this, activityId);
	}

	public void onSignClick(View view) {
		if(isBusiness) {
			IntentManager.startMessageCenterActivity(this, activityId);
		} else {
			IntentManager.startActivitySignActivity(ActivityDetailActivity.this, activityId);
		}
	}

	public void onInviteClick(View view) {
		IntentManager.startInviteActivity(ActivityDetailActivity.this);
	}

	public void onShareClick(View view) {
		shareActivity();
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

	private void wantFinishedActivity() {
		if(Utils.checkNetworkConnected(this)) {
			onShowLoadingDialog();
			HttpManager.wantFinishedActivity(PartnerApplication.getInstance().getUserInfo().getToken(), activityId, new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					onDismissLoadingDialog();
					String responseBody = HttpUtils.getResponseData(response);
					if(!TextUtils.isEmpty(responseBody)) {
						Toaster.show(R.string.operate_success);
						againLayout.setVisibility(View.GONE);
						againView.setVisibility(View.GONE);
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