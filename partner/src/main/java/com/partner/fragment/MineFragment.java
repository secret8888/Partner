package com.partner.fragment;

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
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.PreferenceConsts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.IntentManager;
import com.partner.common.util.PreferenceUtils;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.fragment.base.BaseFragment;
import com.partner.model.UserInfo;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MineFragment extends BaseFragment implements OnClickListener {

	@ViewId(R.id.im_avatar)
	private SimpleDraweeView avatarView;

	@ViewId(R.id.tv_name)
	private TextView nameView;

	@ViewId(R.id.lv_info)
	private RelativeLayout infoLayout;

	@ViewId(R.id.lv_registration_info)
	private RelativeLayout registrationInfoLayout;

	@ViewId(R.id.lv_setting)
	private RelativeLayout settingLayout;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_mine;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {

	}

	@Override
	protected void setListeners() {
		infoLayout.setOnClickListener(this);
		settingLayout.setOnClickListener(this);
		registrationInfoLayout.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (Utils.checkMetworkConnected(getActivity()) && TextUtils.isEmpty(nameView.getText())) {
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
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lv_setting:
			IntentManager.startSettingActivity(getActivity());
			break;
		case R.id.lv_info:
			IntentManager.startMyInfoActivity(getActivity());
			break;
		case R.id.lv_registration_info:
			IntentManager.startRegistrationInfoActivity(getActivity());
			break;
		default:
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
