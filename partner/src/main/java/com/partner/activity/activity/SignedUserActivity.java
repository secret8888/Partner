package com.partner.activity.activity;

import android.os.Bundle;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.adapter.SignedUserAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.IntentConsts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.Utils;
import com.partner.model.ActivityInfo;
import com.partner.model.FriendList;
import com.partner.view.TitleView;
import com.partner.view.refresh.RefreshListView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.youdao.yjson.YJson;

import java.io.IOException;

public class SignedUserActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.list_content)
	private RefreshListView contentView;

	private SignedUserAdapter adapter;

	private FriendList friendList;

	private int activityId;

	private static final String TAG = SignedUserActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_signed_user;
	}

	@Override
	protected void readIntent() {
		activityId = getIntent().getIntExtra(IntentConsts.ID_KEY, -1);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.signed_user);
		contentView.setPullLoadEnable(false);
		contentView.setPullRefreshEnable(false);
		onShowLoadingDialog();
		getSignedUsers();
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	private void onMessageLoad() {
		contentView.stopRefresh();
		contentView.stopLoadMore();
		contentView.setRefreshTime(Utils.getTime());
		onDismissLoadingDialog();
	}

	private void getSignedUsers() {
		if(Utils.checkNetworkConnected(this)) {
			onMessageLoad();
			HttpManager.getSignedUsers(PartnerApplication.getInstance().getUserInfo().getToken(), activityId, new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					handleSignedUser(response);
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onMessageLoad();
				}
			});
		} else {
			onMessageLoad();
		}
	}

	private void handleSignedUser(Response response) {
		onDismissLoadingDialog();
		String responseBody = HttpUtils.getResponseData(response);
		friendList = YJson.getObj(responseBody, FriendList.class);
		adapter = new SignedUserAdapter(this, friendList.getFriends());
		contentView.setAdapter(adapter);
	}
}