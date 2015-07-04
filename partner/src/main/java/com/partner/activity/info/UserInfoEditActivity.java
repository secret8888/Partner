package com.partner.activity.info;

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
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.model.FriendInfo;
import com.partner.view.TitleView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class UserInfoEditActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.im_avatar)
	private SimpleDraweeView avatarView;

	@ViewId(R.id.tv_nickname)
	private TextView nicknameView;

	@ViewId(R.id.tv_note_name)
	private TextView noteNameView;

	private FriendInfo mInfo;

	private static final String TAG = UserInfoEditActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_info_edit;
	}

	@Override
	protected void readIntent() {
		mInfo = (FriendInfo) getIntent().getSerializableExtra(IntentConsts.INFO_KEY);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.app_name);
		titleView.setOperateText(R.string.delete);

		nicknameView.setText(mInfo.getFriendNickName());
		noteNameView.setText(mInfo.getFriendMyName());
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	public void onMessageClick(View view) {
		IntentManager.startLeaveMessageActivity(this);
	}

	@Override
	public void onTitleOperateClick() {
		super.onTitleOperateClick();
		if(Utils.isNetworkConnected(this)) {
			onShowLoadingDialog();
			HttpManager.deleteFriend(PartnerApplication.getInstance().getUserInfo().getToken(),
					mInfo.getFriendId(), new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					super.onRequestResponse(response);
					onDismissLoadingDialog();
					String result = HttpUtils.getResponseData(response);
					if(!TextUtils.isEmpty(result)) {
						Toaster.show(R.string.delete_success);
						setResult(RESULT_OK);
						onBackPressed();
					}
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					super.onRequestFailure(request, e);
					onDismissLoadingDialog();
					Toaster.show(R.string.delete_fail);
				}
			});
		}
	}
}