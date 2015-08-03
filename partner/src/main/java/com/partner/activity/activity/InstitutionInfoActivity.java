package com.partner.activity.activity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.adapter.InstitutionAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.IntentConsts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.Utils;
import com.partner.model.FriendList;
import com.partner.model.InstitutionInfo;
import com.partner.view.TitleView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.youdao.yjson.YJson;

import java.io.IOException;

public class InstitutionInfoActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.im_activity_intro)
	private SimpleDraweeView introView;

	@ViewId(R.id.tv_name)
	private TextView nameView;

	@ViewId(R.id.tv_address)
	private TextView addressView;

	@ViewId(R.id.tv_desc)
	private TextView descView;

	private int id;

	private static final String TAG = InstitutionInfoActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_institution;
	}

	@Override
	protected void readIntent() {
		id = getIntent().getIntExtra(IntentConsts.ID_KEY, -1);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.institution_detail);
		getInstitutionInfo();
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	public void getInstitutionInfo() {
		if(Utils.isNetworkConnected(this)) {
			onShowLoadingDialog();
			HttpManager.getOrgInfo(PartnerApplication.getInstance().getUserInfo().getToken(), id, new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					super.onRequestResponse(response);
					onDismissLoadingDialog();
					String result = HttpUtils.getResponseData(response);
					if (!TextUtils.isEmpty(result)) {
						InstitutionInfo institutionInfo = YJson.getObj(result, InstitutionInfo.class);
						nameView.setText(institutionInfo.getUsername());
						addressView.setText(institutionInfo.getAddress());
						descView.setText(institutionInfo.getDescription());
						if(!TextUtils.isEmpty(institutionInfo.getHeadimage())) {
							introView.setImageURI(Uri.parse(institutionInfo.getHeadimage()));
						}
					}
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					super.onRequestFailure(request, e);
					onDismissLoadingDialog();
				}
			});
		}
	}
}