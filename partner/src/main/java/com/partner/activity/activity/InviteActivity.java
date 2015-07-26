package com.partner.activity.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.MainActivity;
import com.partner.activity.base.BaseActivity;
import com.partner.adapter.FriendAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.IntentManager;
import com.partner.common.util.Utils;
import com.partner.model.FriendList;
import com.partner.view.TitleView;
import com.partner.view.refresh.RefreshListView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.youdao.yjson.YJson;

import java.io.IOException;

public class InviteActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.list_content)
	private ListView contentView;

	private FriendList friendList;

	private static final String TAG = InviteActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_invite;
	}

	@Override
	protected void readIntent() {
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.invite_friend);
		titleView.setOperateText(R.string.finish);
		getFriendsList();
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	@Override
	public void onTitleOperateClick() {
		super.onTitleOperateClick();
		onBackPressed();
	}

	public void getFriendsList() {
		if(Utils.isNetworkConnected(this)) {
			onShowLoadingDialog();
			HttpManager.getFriendsList(PartnerApplication.getInstance().getUserInfo().getToken(), 0, new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					super.onRequestResponse(response);
					onDismissLoadingDialog();
					String result = HttpUtils.getResponseData(response);
					if (!TextUtils.isEmpty(result)) {
						friendList = YJson.getObj(result, FriendList.class);
						FriendAdapter adapter = new FriendAdapter(InviteActivity.this, friendList.getFriends());
						contentView.setAdapter(adapter);
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