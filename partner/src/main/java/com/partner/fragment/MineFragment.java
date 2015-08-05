package com.partner.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.MainActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.Consts;
import com.partner.common.constant.PreferenceConsts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.IntentManager;
import com.partner.common.util.PreferenceUtils;
import com.partner.common.util.Utils;
import com.partner.fragment.base.BaseFragment;
import com.partner.qrcode.activity.CaptureActivity;
import com.partner.qrcode.activity.ResultActivity;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MineFragment extends BaseFragment implements OnClickListener {

	@ViewId(R.id.tv_title)
	private TextView titleView;

	@ViewId(R.id.im_avatar)
	private SimpleDraweeView avatarView;

	@ViewId(R.id.tv_name)
	private TextView nameView;

	@ViewId(R.id.lv_info)
	private RelativeLayout infoLayout;

	@ViewId(R.id.lv_registration_info)
	private RelativeLayout registrationInfoLayout;

	@ViewId(R.id.im_registration)
	private ImageView registrationView;

	@ViewId(R.id.tv_registration)
	private TextView registrationTextView;

	@ViewId(R.id.tv_activity_tip)
	private TextView tipView;

	@ViewId(R.id.lv_qrcode)
	private RelativeLayout qrcodeLayout;

	@ViewId(R.id.im_qrcode)
	private ImageView qrcodeView;

	@ViewId(R.id.tv_qrcode)
	private TextView qrcodeTextView;

	@ViewId(R.id.lv_message_center)
	private RelativeLayout messageCenterLayout;

	@ViewId(R.id.lv_setting)
	private RelativeLayout settingLayout;

	private boolean isBusiness;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_mine;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		isBusiness = PartnerApplication.getInstance().getUserInfo().getUserType() == Consts.ROLE_BUSINESS;
		if(isBusiness) {
			titleView.setText(R.string.mine);
			registrationView.setImageResource(R.drawable.ic_publish);
			registrationTextView.setText(R.string.publish_activity);
			qrcodeView.setImageResource(R.drawable.ic_published);
			qrcodeTextView.setText(R.string.published_activity);
			tipView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void setListeners() {
		infoLayout.setOnClickListener(this);
		settingLayout.setOnClickListener(this);
		registrationInfoLayout.setOnClickListener(this);
		qrcodeLayout.setOnClickListener(this);
		messageCenterLayout.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (Utils.checkNetworkConnected(getActivity()) && TextUtils.isEmpty(nameView.getText())) {
			onShowLoadingDialog();
			HttpManager.getUserInfo(PartnerApplication.getInstance().getUserInfo().getToken(), new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					handleMineResult(response);
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
				}
			});
		} else {
			nameView.setText(PartnerApplication.getInstance().getUserInfo().getUserName());
			String avatarImage = PartnerApplication.getInstance().getUserInfo().getHeadImage();
			if(!TextUtils.isEmpty(avatarImage)) {
				Uri uri = Uri.parse(avatarImage);
				avatarView.setImageURI(uri);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lv_setting:
			IntentManager.startSettingActivity(getActivity(), 0);
			break;
		case R.id.lv_info:
			IntentManager.startMyInfoActivity(getActivity());
			break;
		case R.id.lv_registration_info: //报名常用信息和发布活动
			if(isBusiness) {
				IntentManager.startPublishActivity(getActivity());
			} else {
				IntentManager.startRegistrationInfoActivity(getActivity());
			}
			break;
		case R.id.lv_qrcode: //扫一扫加好友和已经发布活动
			if(isBusiness) {
				IntentManager.startPublishedActivity(getActivity());
			} else {
				IntentManager.startCaptureActivity(getActivity(), 1);
			}
			break;
		case R.id.lv_message_center:
			IntentManager.startMessageCenterActivity(getActivity());
			break;
		}
	}

	private void handleMineResult(Response response) {
		onDismissLoadingDialog();
		String result = HttpUtils.getResponseData(response);
		if(!TextUtils.isEmpty(result)) {
			PreferenceUtils.putString(PreferenceConsts.KEY_USER_INFO, result);
			PartnerApplication.getInstance().initUserInfo();
			nameView.setText(PartnerApplication.getInstance().getUserInfo().getUserName());
			String avatarImage = PartnerApplication.getInstance().getUserInfo().getHeadImage();
			if(!TextUtils.isEmpty(avatarImage)) {
				Uri uri = Uri.parse(avatarImage);
				avatarView.setImageURI(uri);
			}
		}
	}

}
