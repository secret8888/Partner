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
import com.partner.common.constant.IntentConsts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.IntentManager;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.model.FriendInfo;
import com.partner.model.FriendList;
import com.partner.model.RegistrationInfo;
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

	private int activityId;

	private static final String TAG = InviteActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_invite;
	}

	@Override
	protected void readIntent() {
		activityId = getIntent().getIntExtra(IntentConsts.ID_KEY, -1);
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
		inviteFriend();
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
						FriendAdapter adapter = new FriendAdapter(InviteActivity.this, friendList.getFriends(), true);
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

	private void inviteFriend() {
		StringBuilder builder = new StringBuilder();
		for(FriendInfo info : friendList.getFriends()) {
			if(info.isSelected()) {
				builder.append(info.getFriendId());
				builder.append(",");
			}
		}
		String ids = builder.toString();
		if(ids.length() < 1) {
			Toaster.show(R.string.friend_select);
			return;
		}
		if (Utils.checkNetworkConnected(this)) {
			onShowLoadingDialog();
			HttpManager.inviteFriend(PartnerApplication.getInstance().getUserInfo().getToken(), activityId,
					ids, new AsyncHttpCallback() {
						@Override
						public void onRequestResponse(Response response) {
							Toaster.show(R.string.invite_success);
							onBackPressed();
						}

						@Override
						public void onRequestFailure(Request request, IOException e) {
							onDismissLoadingDialog();
						}
					});
		}
	}
}