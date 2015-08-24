package com.partner.activity.info;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.Consts;
import com.partner.common.constant.IntentConsts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.model.FriendInfo;
import com.partner.model.FriendList;
import com.partner.model.UserInfo;
import com.partner.view.TitleView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.youdao.yjson.YJson;

import java.io.IOException;

public class LeaveMessageActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.edit_content)
	private EditText contentEdit;

	private FriendList friendList;

	private int userId;

	private int activityId;

	private boolean isBusiness;

	private static final String TAG = LeaveMessageActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_leave_message;
	}

	@Override
	protected void readIntent() {
		userId = getIntent().getIntExtra(IntentConsts.INFO_KEY, -1);
		activityId = getIntent().getIntExtra(IntentConsts.ID_KEY, -1);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		isBusiness = PartnerApplication.getInstance().getUserInfo().getUserType() == Consts.ROLE_BUSINESS;
		if(isBusiness) {
			titleView.setTitle(R.string.send_notice);
		} else {
			titleView.setTitle(R.string.leave_message);
		}
		titleView.setOperateText(R.string.send);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	@Override
	public void onTitleOperateClick() {
		super.onTitleOperateClick();
		String content = contentEdit.getText().toString();
		if(TextUtils.isEmpty(content)) {
			Toaster.show(R.string.message_content_hint);
			return;
		}

		if(Utils.checkNetworkConnected(this)) {
			onShowLoadingDialog();
			StringBuilder userIdsBuilder = new StringBuilder();
			AsyncHttpCallback callback = new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					onDismissLoadingDialog();
					Toaster.show(R.string.leave_msg_success);
					onBackPressed();
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
				}
			};
			UserInfo userInfo = PartnerApplication.getInstance().getUserInfo();
			if(isBusiness) {
//				for(FriendInfo friendInfo : friendList.getFriends()) {
//					userIdsBuilder.append(friendInfo.getFriendId());
//					userIdsBuilder.append(",");
//				}
				HttpManager.sendMessagesByActivity(userInfo.getToken(), activityId, content, callback);
			} else {
				userIdsBuilder.append(userId);
				HttpManager.sendMessage(userInfo.getToken(), userIdsBuilder.toString(), content, callback);
			}
		}
	}

//	private void getSignedUsers() {
//		if(Utils.checkNetworkConnected(this)) {
//			onShowLoadingDialog();
//			HttpManager.getSignedUsers(PartnerApplication.getInstance().getUserInfo().getToken(), activityId, new AsyncHttpCallback() {
//				@Override
//				public void onRequestResponse(Response response) {
//					onDismissLoadingDialog();
//					String responseBody = HttpUtils.getResponseData(response);
//					friendList = YJson.getObj(responseBody, FriendList.class);
//					if(friendList == null || friendList.getFriends() == null || friendList.getFriends().size() == 0) {
//						Toaster.show(R.string.no_sign_user);
//						onBackPressed();
//					}
//				}
//
//				@Override
//				public void onRequestFailure(Request request, IOException e) {
//					onDismissLoadingDialog();
//				}
//			});
//		}
//	}
}